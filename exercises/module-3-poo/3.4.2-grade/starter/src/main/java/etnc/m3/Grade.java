package etnc.m3;

/**
 * Exercice 3.4.2 — Grade : enum a attribut et methodes.
 *
 * <p>Les constantes, l'attribut soldeBase, le constructeur et getSoldeBase sont
 * FOURNIS. Completez categorie() avec un switch exhaustif (sans default).</p>
 */
public enum Grade {

    SOLDAT(1600), CAPORAL(1800), SERGENT(2200), ADJUDANT(2600), LIEUTENANT(3000);

    private final double soldeBase;

    Grade(double soldeBase) {
        this.soldeBase = soldeBase;
    }

    public double getSoldeBase() {
        return soldeBase;
    }

    /**
     * @return la categorie du grade :
     *         "Militaire du rang" (SOLDAT, CAPORAL),
     *         "Sous-officier" (SERGENT, ADJUDANT),
     *         "Officier" (LIEUTENANT).
     *         Utilisez un switch exhaustif sur this, sans default.
     */
    public String categorie() {
        return null; // TODO
    }
}
