package piscine.m3;

/** Exercice 3.3.3 — Confirme (solution de reference). */
public class Confirme extends Personnel {

    private int anciennete;

    public Confirme(String nom, int anciennete) {
        super(nom);
        this.anciennete = anciennete;
    }

    @Override
    public String niveau() {
        return "Confirmé";
    }

    @Override
    public double solde() {
        return 2200 + 100 * anciennete;
    }
}
