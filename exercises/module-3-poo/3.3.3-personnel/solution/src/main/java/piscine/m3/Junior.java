package piscine.m3;

/** Exercice 3.3.3 — Junior (solution de reference). */
public class Junior extends Personnel {

    public Junior(String nom) {
        super(nom);
    }

    @Override
    public String niveau() {
        return "Junior";
    }

    @Override
    public double solde() {
        return 1600;
    }
}
