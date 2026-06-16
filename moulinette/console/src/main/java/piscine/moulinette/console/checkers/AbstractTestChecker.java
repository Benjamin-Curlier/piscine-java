package piscine.moulinette.console.checkers;

import piscine.moulinette.framework.CheckResult;
import piscine.moulinette.framework.Checker;
import piscine.moulinette.framework.CheckerContext;
import piscine.moulinette.runner.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Base des Checkers de test : compile le code stagiaire + les sources de test, puis exécute
 * via JUnit les classes du jeu de tests ciblé. Chaque sous-classe précise quel répertoire de
 * test sélectionner et lesquels compiler (pour les utilitaires partagés).
 */
abstract class AbstractTestChecker implements Checker {

    protected final JavaToolkit toolkit;

    protected AbstractTestChecker(JavaToolkit toolkit) { this.toolkit = toolkit; }

    /** Sous-dossier (relatif à exerciseRefPath) dont les classes seront SÉLECTIONNÉES à l'exécution. */
    protected abstract Path selectedTestDir(CheckerContext ctx);

    /** Sous-dossiers (relatifs à exerciseRefPath) à COMPILER (inclut les utilitaires partagés). */
    protected abstract List<Path> compiledTestDirs(CheckerContext ctx);

    /** Nom lisible du jeu de tests, pour les messages. */
    protected abstract String label();

    /**
     * Vrai si l'absence de classe de test sélectionnée doit être tolérée (jeu de tests optionnel).
     * Par défaut {@code false} : un jeu de tests vide est une erreur (cas des tests publics, obligatoires).
     * Les tests privés, optionnels (cf. {@code format-exercice.md} §2), surchargent à {@code true}.
     */
    protected boolean optionalWhenEmpty() { return false; }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        String tag = id();
        Path classesMain = ctx.renduPath().resolve(".piscine/build/" + tag + "/classes-main");
        Path classesTest = ctx.renduPath().resolve(".piscine/build/" + tag + "/classes-test");
        String tooling = toolkit.toolingClasspath();
        try {
            ProcessResult comMain = toolkit.compile(List.of(mainSrc), classesMain, tooling);
            if (comMain.exitCode() != 0) {
                return CheckResult.fail("Le code ne compile pas, impossible de lancer les " + label() + ".",
                    "Vérifie d'abord que ton code compile (checker `compile`).");
            }
            String cpCompileTests = tooling + File.pathSeparator + classesMain;
            ProcessResult comTest = toolkit.compile(compiledTestDirs(ctx), classesTest, cpCompileTests);
            if (comTest.exitCode() != 0) {
                return CheckResult.error("Compilation des " + label() + " impossible (problème côté référence) :\n"
                    + CompileChecker.tronquer(comTest.stderr()));
            }
            List<String> select = FqcnExtractor.fqcnsUnder(ctx.exerciseRefPath().resolve(selectedTestDir(ctx)));
            if (select.isEmpty()) {
                return optionalWhenEmpty()
                    ? CheckResult.ok()
                    : CheckResult.error("Aucune classe de test trouvée pour les " + label() + ".");
            }
            String runCp = tooling + File.pathSeparator + classesMain + File.pathSeparator + classesTest;
            ProcessResult run = toolkit.runJUnit(classesTest, runCp, select);
            if (run.exitCode() == 0) return CheckResult.ok();
            return CheckResult.fail(
                "Des " + label() + " ont échoué :\n" + extraitEchecs(run.stdout()),
                "Compare l'attendu et l'obtenu ci-dessus : la différence pointe vers ce qu'il faut corriger.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique sur les " + label() + " : " + e.getMessage());
        }
    }

    /** Un test en échec, prêt à être présenté au stagiaire. */
    private record Echec(String titre, String description, String attendu, String obtenu, String exception) { }

    /**
     * Transforme la sortie brute du ConsoleLauncher en un résumé lisible par un débutant :
     * pour chaque test en échec, son intitulé, et — pour une assertion — la valeur
     * <strong>attendue</strong> et la valeur <strong>obtenue</strong>. Replie sur l'ancien
     * filtre brut si le format n'est pas reconnu (pour ne jamais être moins informatif).
     */
    static String extraitEchecs(String stdout) {
        List<Echec> echecs = parserEchecs(stdout);
        if (echecs.isEmpty()) {
            return filtreBrut(stdout);
        }
        StringBuilder sb = new StringBuilder();
        for (Echec e : echecs) {
            sb.append("✗ ").append(e.titre()).append('\n');
            if (e.description() != null && !e.description().isBlank()) {
                sb.append("   ").append(e.description()).append('\n');
            }
            if (e.attendu() != null) {
                sb.append("   attendu : ").append(e.attendu()).append('\n');
                sb.append("   obtenu  : ").append(e.obtenu() == null ? "(rien)" : e.obtenu()).append('\n');
            } else if (e.exception() != null) {
                sb.append("   a échoué : ").append(e.exception()).append('\n');
            }
            sb.append('\n');
        }
        return sb.toString().stripTrailing();
    }

    /** Parse la section « Failures (N): » du ConsoleLauncher en échecs structurés. */
    private static List<Echec> parserEchecs(String stdout) {
        List<Echec> res = new ArrayList<>();
        String titre = null;
        String description = null;
        String attendu = null;
        String obtenu = null;
        String exception = null;
        boolean dansEchecs = false;
        for (String brute : stdout.split("\n")) {
            String l = brute.strip();
            if (l.startsWith("Failures (")) {
                dansEchecs = true;
                continue;
            }
            if (!dansEchecs) {
                continue;
            }
            if (l.startsWith("Test run finished") || l.startsWith("[")) {
                break; // fin de la section, début du décompte
            }
            if (l.startsWith("JUnit ") && l.contains(":")) {
                if (titre != null) {
                    res.add(new Echec(titre, description, attendu, obtenu, exception));
                }
                titre = l.substring(l.lastIndexOf(':') + 1).strip();
                description = null;
                attendu = null;
                obtenu = null;
                exception = null;
            } else if (l.startsWith("=>")) {
                String apres = l.substring(2).strip();              // "type: message"
                int sep = apres.indexOf(": ");
                String type = sep >= 0 ? apres.substring(0, sep).strip() : apres;
                String message = sep >= 0 ? apres.substring(sep + 2).strip() : "";
                String typeCourt = type.contains(".") ? type.substring(type.lastIndexOf('.') + 1) : type;
                int o = message.indexOf('[');
                int c = message.lastIndexOf(']');
                if (o >= 0 && c > o) {                                // la description AssertJ : [ ... ]
                    description = message.substring(o + 1, c).strip();
                    message = (message.substring(0, o) + message.substring(c + 1)).strip();
                }
                exception = message.isBlank() ? typeCourt : typeCourt + " : " + message;
            } else if (l.startsWith("expected:")) {
                attendu = l.substring("expected:".length()).strip();
            } else if (l.startsWith("but was:")) {
                obtenu = l.substring("but was:".length()).strip();
            } else if (titre == null && l.startsWith("MethodSource")) {
                int i = l.indexOf("methodName = '");
                if (i >= 0) {
                    int j = l.indexOf('\'', i + 14);
                    if (j > 0) {
                        titre = l.substring(i + 14, j);
                    }
                }
            }
        }
        if (titre != null) {
            res.add(new Echec(titre, description, attendu, obtenu, exception));
        }
        return res;
    }

    /** Repli : ancien filtre « lignes de synthèse », utilisé si le format n'est pas reconnu. */
    static String filtreBrut(String stdout) {
        List<String> garde = new ArrayList<>();
        for (String l : stdout.split("\n")) {
            String t = l.strip();
            if (t.contains("tests successful") || t.contains("tests failed")
                || t.startsWith("Failures") || t.startsWith("MethodSource")
                || t.contains("expected") || t.contains("but was")) {
                garde.add(l);
            }
        }
        return garde.isEmpty() ? stdout : String.join("\n", garde);
    }
}
