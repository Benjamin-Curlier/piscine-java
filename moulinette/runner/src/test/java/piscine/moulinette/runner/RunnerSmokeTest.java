package piscine.moulinette.runner;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de fumée du module runner.
 */
class RunnerSmokeTest {

    @Test
    void processRunner_shouldInstantiate() {
        ProcessRunner runner = new ProcessRunner();
        assertThat(runner).isNotNull();
    }

    @Test
    void processResult_exitCodeZero_isSuccess() {
        ProcessResult result = new ProcessResult(0, "Bonjour", "");
        assertThat(result.success()).isTrue();
        assertThat(result.stdout()).isEqualTo("Bonjour");
        assertThat(result.stderr()).isEmpty();
    }

    @Test
    void processResult_nonZeroExitCode_isFailure() {
        ProcessResult result = new ProcessResult(1, "", "error: cannot find symbol");
        assertThat(result.success()).isFalse();
        assertThat(result.stderr()).contains("cannot find symbol");
    }

    @Test
    void processResult_nullOutputs_defaultToEmptyString() {
        ProcessResult result = new ProcessResult(0, null, null);
        assertThat(result.stdout()).isNotNull().isEmpty();
        assertThat(result.stderr()).isNotNull().isEmpty();
    }
}
