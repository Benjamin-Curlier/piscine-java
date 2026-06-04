package etnc.m5;

/**
 * Représente une unité militaire dotée d'un nom, d'une capacité maximale
 * d'accueil et d'un effectif courant.
 *
 * <p>Les champs, le constructeur et les getters sont fournis. À VOUS de compléter
 * les deux méthodes {@code affecter} pour qu'elles lèvent une
 * {@link EffectifInvalideException} quand l'effectif est invalide (cf. {@code sujet.md}).</p>
 */
public class Unite {

    private final String nom;
    private final int capaciteMax;
    private int effectif;

    /**
     * Crée une unité avec un effectif initial nul.
     *
     * @param nom         nom de l'unité (ex. « Alpha »)
     * @param capaciteMax effectif maximal autorisé
     */
    public Unite(String nom, int capaciteMax) {
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
     * Affecte un effectif à l'unité (valeur attendue dans {@code [0, capaciteMax]}).
     *
     * @param effectif nouvel effectif souhaité
     * @throws EffectifInvalideException si {@code effectif} est hors plage
     */
    public void affecter(int effectif) {
        // TODO : si effectif < 0 || effectif > capaciteMax, lever une
        //        EffectifInvalideException dont le message nomme la valeur fautive
        //        ET le nom de l'unité. Sinon, mettre à jour le champ effectif.
    }

    /**
     * Affecte un effectif fourni sous forme de texte.
     *
     * @param effectifTexte effectif sous forme de chaîne (ex. « 30 »)
     * @throws EffectifInvalideException si le texte est illisible (avec cause)
     *                                   ou si la valeur lue est hors plage (sans cause)
     */
    public void affecter(String effectifTexte) {
        // TODO : tenter Integer.parseInt(effectifTexte) ; en cas de
        //        NumberFormatException, relancer une EffectifInvalideException
        //        chaînée (message + cause). Sinon, déléguer à affecter(int).
    }
}
