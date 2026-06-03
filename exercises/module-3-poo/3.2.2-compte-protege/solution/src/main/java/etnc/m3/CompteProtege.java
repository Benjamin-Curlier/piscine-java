package etnc.m3;

import java.util.Locale;

/**
 * Exercice 3.2.2 — CompteProtege (solution de reference).
 *
 * <p>Prolonge le Compte du sous-groupe 3.1 en ajoutant des gardes : solde
 * initial corrige, depots/retraits invalides refuses, jamais de solde negatif.</p>
 */
public class CompteProtege {

    private final String titulaire;
    private double solde;

    public CompteProtege(String titulaire, double soldeInitial) {
        this.titulaire = titulaire;
        this.solde = soldeInitial < 0 ? 0.0 : soldeInitial;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public double getSolde() {
        return solde;
    }

    public void deposer(double montant) {
        if (montant > 0) {
            this.solde += montant;
        }
    }

    public void retirer(double montant) {
        if (montant > 0 && montant <= solde) {
            this.solde -= montant;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Compte de %s : %.2f €", titulaire, solde);
    }
}
