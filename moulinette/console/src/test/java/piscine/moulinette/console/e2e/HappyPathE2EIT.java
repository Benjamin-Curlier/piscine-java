package piscine.moulinette.console.e2e;

import piscine.moulinette.console.ConsoleSession;
import piscine.moulinette.console.Mode;
import piscine.moulinette.console.checkers.*;
import piscine.moulinette.console.commands.CommandRegistry;
import piscine.moulinette.console.git.ProcessGitClient;
import piscine.moulinette.console.repl.*;
import piscine.moulinette.console.trigger.MoulinetteRunner;
import piscine.moulinette.console.trigger.SubmissionTrigger;
import piscine.moulinette.console.workspace.*;
import piscine.moulinette.framework.Checker;
import piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("e2e")
class HappyPathE2EIT {

    @Test
    void bonne_solution_donne_un_rapport_ok(@TempDir Path tmp) throws Exception {
        String out = jouerScenario(tmp, MAIN_OK);
        assertThat(out).contains("rendu/1.1").contains("✓ OK");
    }

    @Test
    void main_vide_bloque_sur_tests_publics(@TempDir Path tmp) throws Exception {
        String out = jouerScenario(tmp, MAIN_VIDE);
        assertThat(out).contains("✗ ÉCHEC");
    }

    private static final String MAIN_OK =
        "package piscine.m1;\npublic class HelloWorld { public static void main(String[] a){ System.out.println(\"Hello, world!\"); } }\n";
    private static final String MAIN_VIDE =
        "package piscine.m1;\npublic class HelloWorld { public static void main(String[] a){ } }\n";

    private static String jouerScenario(Path tmp, String mainCode) throws Exception {
        Path piscine = tmp.resolve("piscine-java");
        ecrireExoReference(piscine, mainCode);

        Path dest = tmp.resolve("piscine-curlier");
        var initializer = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = initializer.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        var catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        var toolkit = new JavaToolkit(new ProcessRunner());
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new StyleChecker(toolkit, StyleChecker.extractBundledConfig()));
        var runner = new MoulinetteRunner.Default(catalog, checkers, ws.repoRoot().resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(ws.repoRoot(), new ProcessGitClient(), catalog, Mode.LOCAL);

        String script = String.join("\n",
            "submit-start 1.1", "git add .", "git commit -m \"rendu 1.1.1\"",
            "git push origin rendu/1.1", "exit", "");
        var sw = new StringWriter();
        var session = ConsoleSession.of(ctx, CommandRegistry.defaults(trigger));
        new Repl(session, new ReplIo(new StringReader(script), sw)).run();
        return sw.toString();
    }

    /** Crée exercises/module-1-fondamentaux/1.1.1-hello-world avec starter (mainCode), tests, util, tests-prives. */
    private static void ecrireExoReference(Path piscine, String mainCode) throws Exception {
        Path exo = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Path starter = exo.resolve("starter/src/main/java/piscine/m1");
        Files.createDirectories(starter);
        Files.writeString(starter.resolve("HelloWorld.java"), mainCode);
        Files.writeString(exo.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Path pub = exo.resolve("tests/src/test/java/piscine/m1");
        Files.createDirectories(pub);
        Files.writeString(pub.resolve("HelloWorldTest.java"), """
            package piscine.m1;
            import piscine.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldTest {
              @Test void affiche() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);
        Path util = exo.resolve("tests/src/test/java/piscine/util");
        Files.createDirectories(util);
        Files.writeString(util.resolve("CaptureSortie.java"), """
            package piscine.util;
            import java.io.ByteArrayOutputStream;
            import java.io.PrintStream;
            import java.nio.charset.StandardCharsets;
            public final class CaptureSortie {
              private CaptureSortie() {}
              public static String capturer(Runnable action) {
                PrintStream original = System.out;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                try (PrintStream c = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
                  System.setOut(c); action.run(); System.out.flush();
                } finally { System.setOut(original); }
                return buffer.toString(StandardCharsets.UTF_8);
              }
            }
            """);
        Path priv = exo.resolve("tests-prives/src/test/java/piscine/m1");
        Files.createDirectories(priv);
        Files.writeString(priv.resolve("HelloWorldPriveTest.java"), """
            package piscine.m1;
            import piscine.util.CaptureSortie;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class HelloWorldPriveTest {
              @Test void ignore_args() {
                String s = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{"x"}));
                assertThat(s).isEqualTo("Hello, world!" + System.lineSeparator());
              }
            }
            """);
    }
}
