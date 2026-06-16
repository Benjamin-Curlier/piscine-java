package piscine.m5;

/**
 * Exception métier signalant qu'un effectif affecté à une équipe est invalide.
 *
 * <p>À COMPLÉTER : cette exception doit hériter de {@link RuntimeException}
 * (elle est <em>unchecked</em>) et fournir DEUX constructeurs :</p>
 * <ul>
 *   <li>{@code EffectifInvalideException(String message)} — déjà fourni ci-dessous ;</li>
 *   <li>{@code EffectifInvalideException(String message, Throwable cause)} — à ajouter,
 *       pour permettre le <strong>chaînage</strong> de la cause (cf. {@code Equipe.affecter(String)}).</li>
 * </ul>
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

    // TODO : ajouter le constructeur (String, Throwable) pour le chaînage.
    //        Il doit appeler super(message, cause).
}
