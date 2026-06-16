package piscine.moulinette.console.checkers;

import piscine.moulinette.framework.CheckResult;
import piscine.moulinette.framework.CheckerContext;
import piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("tools")
class TestCheckersIT {

    private final JavaToolkit tk = new JavaToolkit(new ProcessRunner());

    @Test
    void solution_de_reference_passe_les_tests_publics(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, SOLUTION_MAIN);
        CheckResult r = new PublicTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void main_vide_echoue_aux_tests_publics(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, MAIN_VIDE);
        CheckResult r = new PublicTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    @Test
    void solution_de_reference_passe_les_tests_prives(@TempDir Path tmp) throws IOException {
        var ctx = scenario(tmp, SOLUTION_MAIN);
        CheckResult r = new PrivateTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void tests_prives_absents_sont_ignores(@TempDir Path tmp) throws IOException {
        // tests-prives/ est OPTIONNEL (format §2) : un exo simple sans tests privés
        // ne doit pas échouer, il doit être considéré comme OK (rien à vérifier).
        var ctx = scenario(tmp, SOLUTION_MAIN, false);
        CheckResult r = new PrivateTestChecker(tk).check(ctx);
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    // ── Fixtures ────────────────────────────────────────────────────────────

    private static final String SOLUTION_MAIN =
        "package piscine.m1;\npublic class HelloWorld {\n public static void main(String[] a){\n  System.out.println(\"Hello, world!\");\n }\n}\n";
    private static final String MAIN_VIDE =
        "package piscine.m1;\npublic class HelloWorld {\n public static void main(String[] a){\n }\n}\n";

    /** Construit un rendu + un dossier de référence avec tests publics, privés et l'util CaptureSortie. */
    private static CheckerContext scenario(Path tmp, String mainCode) throws IOException {
        return scenario(tmp, mainCode, true);
    }

    /** Variante : {@code withPrivateTests=false} omet le dossier tests-prives/ (cas optionnel). */
    private static CheckerContext scenario(Path tmp, String mainCode, boolean withPrivateTests) throws IOException {
        Path rendu = tmp.resolve("ws/exo");
        Path mainSrc = rendu.resolve("starter/src/main/java/piscine/m1");
        Files.createDirectories(mainSrc);
        Files.writeString(mainSrc.resolve("HelloWorld.java"), mainCode);

        Path ref = tmp.resolve("piscine/exo");

        Path pub = ref.resolve("tests/src/test/java/piscine/m1");
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

        Path util = ref.resolve("tests/src/test/java/piscine/util");
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

        if (!withPrivateTests) {
            return new CheckerContext("1.1.1", rendu, ref);
        }

        Path priv = ref.resolve("tests-prives/src/test/java/piscine/m1");
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

        return new CheckerContext("1.1.1", rendu, ref);
    }
}
