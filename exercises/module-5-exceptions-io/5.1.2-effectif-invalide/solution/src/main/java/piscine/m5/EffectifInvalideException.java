package piscine.m5;

/**
 * Exception métier signalant qu'un effectif affecté à une équipe est invalide
 * (valeur hors plage [0, capaciteMax], ou texte non interprétable comme entier).
 *
 * <p>Elle hérite de {@link RuntimeException} : c'est une exception <em>unchecked</em>.
 * On n'oblige donc pas l'appelant à la déclarer (`throws`) ni à la rattraper —
 * c'est le choix idiomatique pour une erreur de validation d'argument.</p>
 *
 * <p>Le second constructeur (message + cause) sert au <strong>chaînage</strong> :
 * quand l'effectif provient d'un texte illisible, on conserve la
 * {@link NumberFormatException} d'origine comme cause, sans masquer l'erreur réelle.</p>
 */
public class EffectifInvalideException extends RuntimeException {

    /**
     * Construit l'exception avec un message explicatif (sans cause sous-jacente).
     *
     * @param message description de la valeur fautive
     */
    public EffectifInvalideException(String message) {
        super(message);
    }

    /**
     * Construit l'exception en conservant l'exception d'origine comme cause
     * (chaînage). Utilisé quand l'effectif vient d'un texte illisible : la
     * {@link NumberFormatException} de {@code Integer.parseInt} reste accessible
     * via {@link Throwable#getCause()}.
     *
     * @param message description de la valeur fautive
     * @param cause   exception d'origine ayant déclenché l'erreur
     */
    public EffectifInvalideException(String message, Throwable cause) {
        super(message, cause);
    }
}
