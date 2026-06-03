package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckerContext;

import java.nio.file.Path;
import java.util.List;

/** Lance les tests privés (dossier {@code tests-prives/}). Compile aussi {@code tests/} pour les utilitaires partagés. */
public final class PrivateTestChecker extends AbstractTestChecker {

    public PrivateTestChecker(JavaToolkit toolkit) { super(toolkit); }

    @Override public String id() { return "tests-prives"; }
    @Override protected String label() { return "tests privés"; }

    /** Les tests privés sont optionnels (format §2) : leur absence n'est pas une erreur. */
    @Override protected boolean optionalWhenEmpty() { return true; }

    @Override protected Path selectedTestDir(CheckerContext ctx) {
        return Path.of("tests-prives/src/test/java");
    }
    @Override protected List<Path> compiledTestDirs(CheckerContext ctx) {
        return List.of(
            ctx.exerciseRefPath().resolve("tests/src/test/java"),        // utilitaires (CaptureSortie…)
            ctx.exerciseRefPath().resolve("tests-prives/src/test/java"));
    }
}
