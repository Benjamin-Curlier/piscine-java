package etnc.piscine.moulinette.framework;

import java.util.Map;
import java.util.Objects;

/**
 * Rapport d'évaluation complet d'un rendu d'exercice.
 *
 * <p>Agrège les {@link CheckResult} de tous les {@link Checker} exécutés
 * sur le rendu d'un étudiant pour un exercice donné.
 *
 * @param exerciseId identifiant de l'exercice (ex. {@code "1.1.1"})
 * @param results    résultats indexés par identifiant de vérificateur
 *                   (ex. {@code "compile"} → {@link CheckResult})
 */
public record EvaluationReport(
        String exerciseId,
        Map<String, CheckResult> results
) {

    /** Constructeur compact : défense contre les nulls, copie défensive de la map. */
    public EvaluationReport {
        Objects.requireNonNull(exerciseId, "exerciseId ne peut pas être null");
        results = results == null ? Map.of() : Map.copyOf(results);
    }

    /**
     * Indique si tous les vérificateurs ont retourné {@link CheckResult.Status#OK}.
     *
     * @return {@code true} si et seulement si aucun résultat FAIL ou ERROR
     */
    public boolean isSuccess() {
        return results.values().stream()
                .allMatch(r -> r.status() == CheckResult.Status.OK);
    }

    /**
     * Nombre de vérificateurs ayant retourné {@link CheckResult.Status#FAIL}.
     */
    public long failCount() {
        return results.values().stream()
                .filter(r -> r.status() == CheckResult.Status.FAIL)
                .count();
    }
}
