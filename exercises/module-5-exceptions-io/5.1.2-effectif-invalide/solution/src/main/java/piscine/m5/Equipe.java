package piscine.m5;

/**
 * Représente une équipe dotée d'un nom, d'une capacité maximale
 * d'accueil et d'un effectif courant.
 *
 * <p>Toute tentative d'affecter un effectif invalide (négatif, supérieur à la
 * capacité, ou issu d'un texte illisible) lève une {@link EffectifInvalideException}.
 * Le champ {@code effectif} n'est mis à jour que lorsque la valeur est valide :
 * en cas d'échec, l'état précédent est conservé intact.</p>
 */
public class Equipe {

    private final String nom;
    private final int capaciteMax;
    private int effectif;

    /**
     * Crée une équipe avec un effectif initial nul.
     *
     * @param nom         nom de l'équipe (ex. « Alpha »)
     * @param capaciteMax effectif maximal autorisé
     */
    public Equipe(String nom, int capaciteMax) {
        this.nom = nom;
        this.capaciteMax = capaciteMax;
        this.effectif = 0;
    }

    public String getNom() {
        return nom;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public int getEffectif() {
        return effectif;
    }

    /**
     * Affecte un effectif à l'équipe.
     *
     * <p>La valeur doit appartenir à l'intervalle {@code [0, capaciteMax]}.
     * Sinon, on lève une {@link EffectifInvalideException} dont le message nomme
     * la valeur fautive ET l'équipe concernée ; l'effectif courant reste inchangé.</p>
     *
     * @param effectif nouvel effectif souhaité
     * @throws EffectifInvalideException si {@code effectif} est hors plage
     */
    public void affecter(int effectif) {
        // Garde de plage : on rejette avant toute mutation, pour ne pas corrompre l'état.
        if (effectif < 0 || effectif > capaciteMax) {
            throw new EffectifInvalideException(
                "Effectif invalide " + effectif + " pour l'equipe " + nom
                + " (plage autorisee : 0.." + capaciteMax + ")");
        }
        this.effectif = effectif;
    }

    /**
     * Affecte un effectif fourni sous forme de texte.
     *
     * <p>On tente d'interpréter le texte comme un entier. Si le texte est illisible
     * ({@link NumberFormatException}), on relance une {@link EffectifInvalideException}
     * en <strong>chaînant</strong> la cause d'origine (accessible via
     * {@link Throwable#getCause()}). Si le texte est un entier, on délègue à
     * {@link #affecter(int)} : la garde de plage s'applique alors, mais l'exception
     * éventuelle n'a <em>pas</em> de cause (ce n'est pas une erreur de lecture).</p>
     *
     * @param effectifTexte effectif sous forme de chaîne (ex. « 30 »)
     * @throws EffectifInvalideException si le texte est illisible (avec cause)
     *                                   ou si la valeur lue est hors plage (sans cause)
     */
    public void affecter(String effectifTexte) {
        int valeur;
        try {
            valeur = Integer.parseInt(effectifTexte);
        } catch (NumberFormatException e) {
            // Chaînage : on conserve la NumberFormatException d'origine comme cause.
            throw new EffectifInvalideException(
                "Effectif illisible : " + effectifTexte, e);
        }
        // Texte interprétable : la garde de plage (sans cause) s'applique ici.
        affecter(valeur);
    }
}
