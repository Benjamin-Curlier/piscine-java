package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckerContext;

import java.nio.file.Path;
import java.util.List;

/** Lance les tests publics (dossier {@code tests/}). */
public final class PublicTestChecker extends AbstractTestChecker {

    public PublicTestChecker(JavaToolkit toolkit) { super(toolkit); }

    @Override public String id() { return "tests-publics"; }
    @Override protected String label() { return "tests publics"; }

    @Override protected Path selectedTestDir(CheckerContext ctx) {
        return Path.of("tests/src/test/java");
    }
    @Override protected List<Path> compiledTestDirs(CheckerContext ctx) {
        return List.of(ctx.exerciseRefPath().resolve("tests/src/test/java"));
    }
}
