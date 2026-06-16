package piscine.m3;

/**
 * Exercice 3.4.2 — Niveau (solution de reference) : enum a attribut et methodes.
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

    public String categorie() {
        return switch (this) {
            case JUNIOR, CONFIRME -> "Débutant";
            case SENIOR, LEAD -> "Intermédiaire";
            case PRINCIPAL -> "Expert";
        };
    }
}
