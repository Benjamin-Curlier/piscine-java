package piscine.m3;

/**
 * Exercice 3.4.2 — Niveau : enum a attribut et methodes.
 *
 * <p>Les constantes, l'attribut soldeBase, le constructeur et getSoldeBase sont
 * FOURNIS. Completez categorie() avec un switch exhaustif (sans default).</p>
 */
public enum Niveau {

    JUNIOR(1600), CONFIRME(1800), SENIOR(2200), LEAD(2600), PRINCIPAL(3000);

    private final double soldeBase;

    Niveau(double soldeBase) {
        this.soldeBase = soldeBase;
    }

    public double getSoldeBase() {
        return soldeBase;
    }

    /**
     * @return la categorie du niveau :
     *         "Débutant" (JUNIOR, CONFIRME),
     *         "Intermédiaire" (SENIOR, LEAD),
     *         "Expert" (PRINCIPAL).
     *         Utilisez un switch exhaustif sur this, sans default.
     */
    public String categorie() {
        return null; // TODO
    }
}
