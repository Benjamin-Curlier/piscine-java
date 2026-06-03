package etnc.m3;

/** Exercice 3.3.3 — Officier (solution de reference). */
public class Officier extends Personnel {

    private int echelon;

    public Officier(String nom, int echelon) {
        super(nom);
        this.echelon = echelon;
    }

    @Override
    public String grade() {
        return "Officier";
    }

    @Override
    public double solde() {
        return 3000 + 200 * echelon;
    }
}
