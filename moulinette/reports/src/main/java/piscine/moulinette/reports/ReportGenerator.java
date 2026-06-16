package piscine.moulinette.reports;

import piscine.moulinette.framework.CheckResult;
import piscine.moulinette.framework.EvaluationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Génère le rapport pédagogique à partir d'un {@link EvaluationReport}.
 *
 * <p>Deux formats sont produits :
 * <ul>
 *   <li><strong>Markdown</strong> — rapport lisible destiné à l'étudiant.</li>
 *   <li><strong>JSON</strong> — données structurées pour la moulinette et les outils d'analyse.</li>
 * </ul>
 *
 * <p><em>Implémentation</em> : concaténation de chaînes dans cette version initiale.
 * Un moteur de templates (Mustache, FreeMarker…) pourra être introduit ultérieurement
 * quand le format des rapports sera stabilisé.
 */
public class ReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerator.class);

    /**
     * Génère le rapport au format Markdown.
     *
     * @param report rapport d'évaluation à formater
     * @return contenu Markdown, jamais null
     */
    public String toMarkdown(EvaluationReport report) {
        Objects.requireNonNull(report, "report ne peut pas être null");
        log.debug("Génération Markdown pour l'exercice {}", report.exerciseId());

        StringBuilder sb = new StringBuilder();
        sb.append("# Résultat — Exercice ").append(report.exerciseId()).append("\n\n");

        if (report.isSuccess()) {
            sb.append("✅ **Tous les contrôles ont réussi. Bravo !**\n\n");
        } else {
            sb.append("❌ **Des problèmes ont été détectés dans votre rendu.**\n\n");
        }

        for (Map.Entry<String, CheckResult> entry : report.results().entrySet()) {
            String checkerId = entry.getKey();
            CheckResult result = entry.getValue();

            sb.append("## ").append(checkerId).append("\n\n");
            sb.append("**Statut** : `").append(result.status()).append("`\n\n");

            appendListSection(sb, "### Problèmes détectés", result.messages());
            appendListSection(sb, "### Suggestions de correction", result.suggestions());
        }
        return sb.toString();
    }

    /**
     * Génère le rapport au format JSON minimaliste.
     *
     * <p>Note : pas de dépendance Jackson dans cette version skeleton.
     * À remplacer par une sérialisation JSON robuste dès que le format est figé.
     *
     * @param report rapport d'évaluation à sérialiser
     * @return JSON valide, jamais null
     */
    public String toJson(EvaluationReport report) {
        Objects.requireNonNull(report, "report ne peut pas être null");
        log.debug("Génération JSON pour l'exercice {}", report.exerciseId());

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"exerciseId\": ").append(jsonString(report.exerciseId())).append(",\n");
        sb.append("  \"success\": ").append(report.isSuccess()).append(",\n");
        sb.append("  \"failCount\": ").append(report.failCount()).append(",\n");
        sb.append("  \"results\": {\n");

        List<Map.Entry<String, CheckResult>> entries = report.results().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<String, CheckResult> entry = entries.get(i);
            CheckResult result = entry.getValue();

            sb.append("    ").append(jsonString(entry.getKey())).append(": {\n");
            sb.append("      \"status\": ").append(jsonString(result.status().name())).append(",\n");
            sb.append("      \"messages\": ").append(jsonStringArray(result.messages())).append(",\n");
            sb.append("      \"suggestions\": ").append(jsonStringArray(result.suggestions())).append("\n");
            sb.append("    }");
            if (i < entries.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("  }\n}");
        return sb.toString();
    }

    // ── Helpers privés ────────────────────────────────────────────────────────

    private static void appendListSection(StringBuilder sb, String header, List<String> items) {
        if (items.isEmpty()) return;
        sb.append(header).append("\n\n");
        items.forEach(item -> sb.append("- ").append(item).append("\n"));
        sb.append("\n");
    }

    private static String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r")
                        .replace("\t", "\\t") + "\"";
    }

    private static String jsonStringArray(List<String> items) {
        if (items.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            sb.append(jsonString(items.get(i)));
            if (i < items.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
