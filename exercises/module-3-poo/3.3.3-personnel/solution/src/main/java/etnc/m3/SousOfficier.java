package etnc.m3;

/** Exercice 3.3.3 — SousOfficier (solution de reference). */
public class SousOfficier extends Personnel {

    private int anciennete;

    public SousOfficier(String nom, int anciennete) {
        super(nom);
        this.anciennete = anciennete;
    }

    @Override
    public String grade() {
        return "Sous-officier";
    }

    @Override
    public double solde() {
        return 2200 + 100 * anciennete;
    }
}
