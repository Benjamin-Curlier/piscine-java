package piscine.moulinette.framework;

import java.util.List;
import java.util.Objects;

/**
 * Résultat produit par un {@link Checker}.
 *
 * <p>Immuable. Utilisez les fabriques statiques pour les cas courants :
 * {@link #ok()}, {@link #fail(String, String)}, {@link #error(String)}.
 *
 * @param status      statut global du vérificateur
 * @param messages    messages pédagogiques affichés à l'étudiant
 * @param suggestions corrections-types commentées (peut être vide)
 */
public record CheckResult(
        Status status,
        List<String> messages,
        List<String> suggestions
) {

    /**
     * Statuts possibles pour un résultat de vérification.
     */
    public enum Status {
        /** La vérification a réussi : aucun problème détecté. */
        OK,
        /** Un ou plusieurs problèmes ont été détectés dans le rendu. */
        FAIL,
        /** Le vérificateur n'a pas pu s'exécuter (erreur interne, timeout, etc.). */
        ERROR
    }

    /** Constructeur compact : défense contre les nulls, copies défensives. */
    public CheckResult {
        Objects.requireNonNull(status, "status ne peut pas être null");
        messages    = messages    == null ? List.of() : List.copyOf(messages);
        suggestions = suggestions == null ? List.of() : List.copyOf(suggestions);
    }

    // ── Fabriques ────────────────────────────────────────────────────────────

    /** Résultat {@link Status#OK} sans message ni suggestion. */
    public static CheckResult ok() {
        return new CheckResult(Status.OK, List.of(), List.of());
    }

    /**
     * Résultat {@link Status#FAIL} avec un message pédagogique et une suggestion.
     *
     * @param message    explication claire du problème (destinée à l'étudiant)
     * @param suggestion correction-type commentée
     */
    public static CheckResult fail(String message, String suggestion) {
        Objects.requireNonNull(message,    "message ne peut pas être null");
        Objects.requireNonNull(suggestion, "suggestion ne peut pas être null");
        return new CheckResult(Status.FAIL, List.of(message), List.of(suggestion));
    }

    /**
     * Résultat {@link Status#ERROR} avec un message d'erreur interne.
     *
     * @param errorMessage description de l'erreur technique
     */
    public static CheckResult error(String errorMessage) {
        Objects.requireNonNull(errorMessage, "errorMessage ne peut pas être null");
        return new CheckResult(Status.ERROR, List.of(errorMessage), List.of());
    }
}
