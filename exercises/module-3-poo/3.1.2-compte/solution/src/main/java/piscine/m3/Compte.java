package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.1.2 — Compte bancaire simple (solution de reference).
 *
 * <p>Illustre un etat mutable pilote par des methodes. NB : {@link #retirer}
 * n'impose aucune garde de decouvert (le solde peut devenir negatif) ; la
 * validation des invariants est l'objet du sous-groupe 3.2.</p>
 */
public class Compte {

    private final String titulaire;
    private double solde;

    public Compte(String titulaire, double soldeInitial) {
        this.titulaire = titulaire;
        this.solde = soldeInitial;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public double getSolde() {
        return solde;
    }

    /** Credite le compte de {@code montant}. */
    public void deposer(double montant) {
        this.solde += montant;
    }

    /** Debite le compte de {@code montant} (sans controle de decouvert). */
    public void retirer(double montant) {
        this.solde -= montant;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Compte de %s : %.2f €", titulaire, solde);
    }
}
