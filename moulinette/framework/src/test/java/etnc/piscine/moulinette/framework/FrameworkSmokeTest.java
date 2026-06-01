package etnc.piscine.moulinette.framework;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test de fumée du module framework.
 * Vérifie que les types de base s'instancient et se comportent correctement.
 */
class FrameworkSmokeTest {

    // ── CheckResult ───────────────────────────────────────────────────────────

    @Test
    void checkResult_ok_shouldHaveOkStatusAndNoMessages() {
        CheckResult result = CheckResult.ok();

        assertThat(result.status()).isEqualTo(CheckResult.Status.OK);
        assertThat(result.messages()).isEmpty();
        assertThat(result.suggestions()).isEmpty();
    }

    @Test
    void checkResult_fail_shouldCarryMessageAndSuggestion() {
        CheckResult result = CheckResult.fail("Indentation incorrecte.", "Utilisez 4 espaces.");

        assertThat(result.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(result.messages()).containsExactly("Indentation incorrecte.");
        assertThat(result.suggestions()).containsExactly("Utilisez 4 espaces.");
    }

    @Test
    void checkResult_error_shouldHaveErrorStatus() {
        CheckResult result = CheckResult.error("Timeout dépassé.");

        assertThat(result.status()).isEqualTo(CheckResult.Status.ERROR);
        assertThat(result.messages()).containsExactly("Timeout dépassé.");
    }

    // ── CheckerContext ────────────────────────────────────────────────────────

    @Test
    void checkerContext_shouldStoreFields() {
        Path path = Path.of("/tmp/rendu");
        CheckerContext ctx = new CheckerContext("1.1.1", path, Path.of("/tmp/ref"));

        assertThat(ctx.exerciseId()).isEqualTo("1.1.1");
        assertThat(ctx.renduPath()).isEqualTo(path);
        assertThat(ctx.exerciseRefPath()).isEqualTo(Path.of("/tmp/ref"));
    }

    @Test
    void checkerContext_blankExerciseId_shouldThrow() {
        assertThatThrownBy(() -> new CheckerContext("  ", Path.of("/tmp"), Path.of("/ref")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exerciseId");
    }

    // ── EvaluationReport ──────────────────────────────────────────────────────

    @Test
    void evaluationReport_allOk_isSuccess() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of("compile", CheckResult.ok(), "style", CheckResult.ok())
        );

        assertThat(report.isSuccess()).isTrue();
        assertThat(report.failCount()).isZero();
    }

    @Test
    void evaluationReport_withOneFail_isNotSuccess() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of(
                        "compile", CheckResult.ok(),
                        "style",   CheckResult.fail("Indentation incorrecte.", "Utilisez 4 espaces.")
                )
        );

        assertThat(report.isSuccess()).isFalse();
        assertThat(report.failCount()).isEqualTo(1L);
    }
}
