package etnc.m3;

/**
 * Exercice 3.1.2 — Compte bancaire simple.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus. A ce stade, {@code retirer} n'impose AUCUN
 * controle de decouvert (le solde peut devenir negatif).</p>
 */
public class Compte {

    private final String titulaire;
    private double solde;

    /** Ouvre un compte au nom de {@code titulaire} avec un solde de {@code soldeInitial}. */
    public Compte(String titulaire, double soldeInitial) {
        this.titulaire = titulaire;
        // TODO : initialiser le solde
    }

    /** @return le nom du titulaire. */
    public String getTitulaire() {
        return null; // TODO
    }

    /** @return le solde courant. */
    public double getSolde() {
        return 0; // TODO
    }

    /** Credite le compte de {@code montant}. */
    public void deposer(double montant) {
        // TODO
    }

    /** Debite le compte de {@code montant} (sans controle de decouvert a ce stade). */
    public void retirer(double montant) {
        // TODO
    }

    /**
     * @return une chaine de la forme {@code "Compte de <titulaire> : <solde> €"}
     *         avec le solde a deux decimales et un point decimal
     *         (ex. {@code "Compte de Dupont : 100.00 €"}). Utilisez
     *         {@code java.util.Locale.ROOT}.
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
