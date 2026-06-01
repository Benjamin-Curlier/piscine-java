package etnc.piscine.moulinette.console.trigger;

import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;
import etnc.piscine.moulinette.console.workspace.ExerciseEntry;
import etnc.piscine.moulinette.console.workspace.SousGroupe;
import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.framework.EvaluationReport;
import etnc.piscine.moulinette.reports.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lance la moulinette sur un sous-groupe d'exercices.
 *
 * <p>Les exercices sont évalués dans l'ordre de difficulté croissante (position),
 * et l'évaluation s'arrête au premier exercice qui échoue.
 */
public interface MoulinetteRunner {

    GroupReport runGroup(String sousGroupeId, Path repoRoot);

    record GroupReport(String sousGroupeId, List<ExoOutcome> outcomes, boolean stoppedEarly, Path reportPath) {}

    record ExoOutcome(String exoId, boolean ok, String message) {}

    /**
     * Implémentation par défaut : exécute une liste de {@link Checker} sur chaque exercice du groupe,
     * écrit un rapport Markdown horodaté, et s'arrête au premier exercice en échec.
     */
    final class Default implements MoulinetteRunner {
        private static final Logger LOG = LoggerFactory.getLogger(Default.class);
        private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");

        private final ExerciseCatalog catalog;
        private final List<Checker> checkers;
        private final Path reportsDir;

        public Default(ExerciseCatalog catalog, List<Checker> checkers, Path reportsDir) {
            this.catalog = catalog;
            this.checkers = List.copyOf(checkers);
            this.reportsDir = reportsDir;
        }

        @Override
        public GroupReport runGroup(String sgId, Path repoRoot) {
            SousGroupe sg = catalog.sousGroupe(sgId);
            List<ExoOutcome> outcomes = new ArrayList<>();
            boolean stopped = false;
            for (ExerciseEntry e : sg.exercices()) {
                Path renduDir = repoRoot.resolve("exercises").resolve(e.exerciseDir().getFileName());
                nettoyerBuildDir(renduDir);
                Map<String, CheckResult> parChecker = new LinkedHashMap<>();
                boolean ok = true;
                StringBuilder msg = new StringBuilder();
                for (Checker c : checkers) {
                    CheckResult r = c.check(new CheckerContext(e.id(), renduDir, e.exerciseDir()));
                    parChecker.put(c.id(), r == null ? CheckResult.error("résultat null") : r);
                    boolean nonOk = (r == null || r.status() != CheckResult.Status.OK);
                    if (nonOk && r != null) {
                        msg.append(c.id()).append(c.isBlocking() ? " : " : " (conseil) : ")
                           .append(String.join(" / ", r.messages())).append('\n');
                    }
                    // Un Checker advisory (ex. style en beta, isBlocking()==false) est rapporté
                    // mais ne fait pas échouer l'exo et n'arrête pas la chaîne. Voir backlog #53.
                    if (nonOk && (r == null || c.isBlocking())) {
                        ok = false;
                        break;
                    }
                }
                ecrireRapportExo(renduDir, e.id(), parChecker);
                outcomes.add(new ExoOutcome(e.id(), ok, msg.toString()));
                if (!ok) { stopped = true; break; }
            }
            Path report = writeReport(sgId, outcomes, stopped);
            return new GroupReport(sgId, outcomes, stopped, report);
        }

        private void nettoyerBuildDir(Path renduDir) {
            Path build = renduDir.resolve(".piscine/build");
            if (Files.isDirectory(build)) {
                try (var walk = Files.walk(build)) {
                    walk.sorted(java.util.Comparator.reverseOrder())
                        .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignore) { } });
                } catch (IOException ignore) { }
            }
        }

        private void ecrireRapportExo(Path renduDir, String exoId, Map<String, CheckResult> parChecker) {
            try {
                Path dir = renduDir.resolve(".piscine/reports");
                Files.createDirectories(dir);
                var report = new EvaluationReport(exoId, parChecker);
                var gen = new ReportGenerator();
                Files.writeString(dir.resolve(exoId + ".md"), gen.toMarkdown(report));
                Files.writeString(dir.resolve(exoId + ".json"), gen.toJson(report));
            } catch (IOException ignore) { /* rapport best-effort */ }
        }

        private Path writeReport(String sgId, List<ExoOutcome> outcomes, boolean stopped) {
            try {
                Files.createDirectories(reportsDir);
                Path out = reportsDir.resolve(sgId + "-" + LocalDateTime.now().format(TS) + ".md");
                StringBuilder sb = new StringBuilder();
                sb.append("# Rapport moulinette — sous-groupe ").append(sgId).append("\n\n");
                for (ExoOutcome o : outcomes) {
                    sb.append("## Exo ").append(o.exoId()).append(" — ")
                      .append(o.ok() ? "✓ OK" : "✗ ÉCHEC").append("\n\n");
                    if (!o.ok() && !o.message().isBlank()) {
                        sb.append("```\n").append(o.message()).append("```\n\n");
                    }
                }
                if (stopped) {
                    sb.append("> Évaluation arrêtée au premier exercice en échec. "
                            + "Corrige-le puis re-pousse la branche.\n");
                }
                Files.writeString(out, sb.toString());
                LOG.info("Rapport écrit : {}", out);
                return out;
            } catch (IOException ioe) {
                throw new UncheckedIOException("écriture du rapport échouée", ioe);
            }
        }
    }
}
