package etnc.m3;

/**
 * Exercice 3.4.2 — Grade (solution de reference) : enum a attribut et methodes.
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

    public String categorie() {
        return switch (this) {
            case SOLDAT, CAPORAL -> "Militaire du rang";
            case SERGENT, ADJUDANT -> "Sous-officier";
            case LIEUTENANT -> "Officier";
        };
    }
}
