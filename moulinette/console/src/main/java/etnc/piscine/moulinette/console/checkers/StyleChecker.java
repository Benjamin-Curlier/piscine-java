package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Analyse de style avec Checkstyle (config bundlée). PMD viendra en fast-follow. */
public final class StyleChecker implements Checker {

    private final JavaToolkit toolkit;
    private final Path config;

    public StyleChecker(JavaToolkit toolkit, Path config) {
        this.toolkit = toolkit;
        this.config = config;
    }

    @Override public String id() { return "style"; }

    /** Beta : le style est advisory (rapporté mais non bloquant). Voir backlog #53. */
    @Override public boolean isBlocking() { return false; }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        try {
            ProcessResult r = toolkit.checkstyle(ctx.renduPath(), config, List.of(mainSrc));
            if (r.exitCode() == 0) return CheckResult.ok();
            List<String> violations = new ArrayList<>();
            for (String l : r.stdout().split("\n")) {
                String t = l.strip();
                if (t.startsWith("[WARN]") || t.startsWith("[ERROR]")) violations.add(t);
            }
            String corps = violations.isEmpty() ? r.stdout() : String.join("\n", violations);
            return CheckResult.fail(
                "Conseils de style (non bloquant) :\n" + corps,
                "Tu peux améliorer l'indentation, les accolades et les imports inutilisés — "
                + "ça ne bloque pas ta validation pendant la beta.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique de l'analyse de style : " + e.getMessage());
        }
    }

    /** Extrait la config Checkstyle bundlée dans un fichier temporaire et renvoie son chemin. */
    public static Path extractBundledConfig() {
        try (InputStream in = StyleChecker.class.getResourceAsStream("/checkstyle-etnc.xml")) {
            if (in == null) throw new IllegalStateException("checkstyle-etnc.xml introuvable dans les resources");
            Path tmp = Files.createTempFile("checkstyle-etnc", ".xml");
            tmp.toFile().deleteOnExit();
            Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
            return tmp;
        } catch (IOException e) {
            throw new UncheckedIOException("extraction config Checkstyle échouée", e);
        }
    }
}
