package etnc.piscine.moulinette.reports;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.EvaluationReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de fumée du module reports.
 */
class ReportsSmokeTest {

    private ReportGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ReportGenerator();
    }

    // ── toMarkdown ────────────────────────────────────────────────────────────

    @Test
    void toMarkdown_allOk_shouldContainSuccessIcon() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of("compile", CheckResult.ok())
        );
        String md = generator.toMarkdown(report);

        assertThat(md).contains("1.1.1");
        assertThat(md).contains("✅");
        assertThat(md).doesNotContain("❌");
    }

    @Test
    void toMarkdown_withFail_shouldContainFailureIconAndMessage() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of(
                        "compile", CheckResult.fail(
                                "Erreur de compilation : symbole introuvable.",
                                "Vérifiez que la classe `HelloWorld` est déclarée avec un `H` majuscule."
                        )
                )
        );
        String md = generator.toMarkdown(report);

        assertThat(md).contains("❌");
        assertThat(md).contains("Erreur de compilation");
        assertThat(md).contains("HelloWorld");
        assertThat(md).doesNotContain("✅");
    }

    // ── toJson ────────────────────────────────────────────────────────────────

    @Test
    void toJson_allOk_shouldBeValidStructure() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of("compile", CheckResult.ok())
        );
        String json = generator.toJson(report);

        assertThat(json).contains("\"exerciseId\": \"1.1.1\"");
        assertThat(json).contains("\"success\": true");
        assertThat(json).contains("\"failCount\": 0");
        assertThat(json).contains("\"compile\"");
        assertThat(json).contains("\"OK\"");
    }

    @Test
    void toJson_withFail_successShouldBeFalse() {
        EvaluationReport report = new EvaluationReport(
                "1.1.1",
                Map.of("style", CheckResult.fail("Indentation.", "4 espaces."))
        );
        String json = generator.toJson(report);

        assertThat(json).contains("\"success\": false");
        assertThat(json).contains("\"failCount\": 1");
        assertThat(json).contains("\"FAIL\"");
    }
}
