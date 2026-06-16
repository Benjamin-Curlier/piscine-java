package piscine.moulinette.console.checkers;

import piscine.moulinette.framework.CheckerContext;
import piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sur un exo « écriture de tests » (dossier {@code mutants/}), seul le {@link MutationChecker}
 * s'applique ; les checkers normaux se désactivent. Sur un exo classique, l'inverse.
 */
class CheckersAppliesToTest {

    private final JavaToolkit tk = new JavaToolkit(new ProcessRunner());

    @Test
    void sur_exo_ecriture_de_tests_seul_mutation_s_applique(@TempDir Path tmp) throws IOException {
        Path ref = tmp.resolve("piscine/exo");
        Files.createDirectories(ref.resolve("mutants/op/piscine/m6"));
        Path rendu = tmp.resolve("ws/exo");
        Files.createDirectories(rendu);
        var ctx = new CheckerContext("6.1.1", rendu, ref);

        assertThat(new MutationChecker(tk).appliesTo(ctx)).isTrue();
        assertThat(new PublicTestChecker(tk).appliesTo(ctx)).isFalse();
        assertThat(new PrivateTestChecker(tk).appliesTo(ctx)).isFalse();
        assertThat(new CompileChecker(tk).appliesTo(ctx)).isFalse();
    }

    @Test
    void sur_exo_classique_mutation_ne_s_applique_pas(@TempDir Path tmp) throws IOException {
        Path ref = tmp.resolve("piscine/exo");
        Files.createDirectories(ref); // pas de dossier mutants/
        Path rendu = tmp.resolve("ws/exo");
        Files.createDirectories(rendu);
        var ctx = new CheckerContext("1.1.1", rendu, ref);

        assertThat(new MutationChecker(tk).appliesTo(ctx)).isFalse();
        assertThat(new PublicTestChecker(tk).appliesTo(ctx)).isTrue();
        assertThat(new PrivateTestChecker(tk).appliesTo(ctx)).isTrue();
        assertThat(new CompileChecker(tk).appliesTo(ctx)).isTrue();
    }
}
