package piscine.m3;

/** Exercice 3.3.3 — Senior (solution de reference). */
public class Senior extends Personnel {

    private int echelon;

    public Senior(String nom, int echelon) {
        super(nom);
        this.echelon = echelon;
    }

    @Override
    public String niveau() {
        return "Senior";
    }

    @Override
    public double solde() {
        return 3000 + 200 * echelon;
    }
}
