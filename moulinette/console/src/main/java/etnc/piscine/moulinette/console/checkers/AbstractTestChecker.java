package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

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
                return CheckResult.error("Aucune classe de test trouvée pour les " + label() + ".");
            }
            String runCp = tooling + File.pathSeparator + classesMain + File.pathSeparator + classesTest;
            ProcessResult run = toolkit.runJUnit(classesTest, runCp, select);
            if (run.exitCode() == 0) return CheckResult.ok();
            return CheckResult.fail(
                "Des " + label() + " ont échoué :\n" + extraitEchecs(run.stdout()),
                "Relis le sujet et corrige ton code pour faire passer ces tests.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique sur les " + label() + " : " + e.getMessage());
        }
    }

    /** Garde la section de synthèse du ConsoleLauncher (lignes 'tests ...' + 'Failures'). */
    static String extraitEchecs(String stdout) {
        List<String> garde = new ArrayList<>();
        for (String l : stdout.split("\n")) {
            String t = l.strip();
            if (t.contains("tests successful") || t.contains("tests failed")
                || t.startsWith("Failures") || t.startsWith("MethodSource")
                || t.contains("==> expected")) {
                garde.add(l);
            }
        }
        return garde.isEmpty() ? stdout : String.join("\n", garde);
    }
}
