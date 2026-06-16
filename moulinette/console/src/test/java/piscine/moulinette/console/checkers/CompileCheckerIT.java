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
class CompileCheckerIT {

    private final CompileChecker checker = new CompileChecker(new JavaToolkit(new ProcessRunner()));

    @Test
    void code_correct_compile(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, "public class HelloWorld { public static void main(String[] a){ System.out.println(\"hi\"); } }");
        CheckResult r = checker.check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void code_avec_erreur_de_syntaxe_echoue(@TempDir Path tmp) throws IOException {
        Path rendu = ecrireMain(tmp, "public class HelloWorld { oops }");
        CheckResult r = checker.check(new CheckerContext("1.1.1", rendu, tmp.resolve("ref")));
        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    private static Path ecrireMain(Path tmp, String code) throws IOException {
        Path rendu = tmp.resolve("exo");
        Path src = rendu.resolve("starter/src/main/java");
        Files.createDirectories(src);
        Files.writeString(src.resolve("HelloWorld.java"), code);
        return rendu;
    }
}
