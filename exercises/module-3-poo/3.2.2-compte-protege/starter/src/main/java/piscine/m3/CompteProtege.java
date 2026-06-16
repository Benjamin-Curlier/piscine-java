package piscine.m3;

/**
 * Exercice 3.2.2 — CompteProtege : un compte avec des gardes.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus. Aucune exception : une operation invalide est
 * corrigee (constructeur) ou refusee (l'objet reste inchange).</p>
 */
public class CompteProtege {

    private final String titulaire;
    private double solde;

    /**
     * Ouvre un compte. Invariant : le solde n'est jamais negatif.
     * Un {@code soldeInitial} strictement negatif est corrige a {@code 0.0}.
     */
    public CompteProtege(String titulaire, double soldeInitial) {
        this.titulaire = titulaire;
        // TODO : initialiser le solde en corrigeant un soldeInitial < 0 a 0.0
    }

    /** @return le nom du titulaire. */
    public String getTitulaire() {
        return null; // TODO
    }

    /** @return le solde courant. */
    public double getSolde() {
        return 0; // TODO
    }

    /** Credite le compte si {@code montant > 0} ; sinon ne fait rien. */
    public void deposer(double montant) {
        // TODO
    }

    /**
     * Debite le compte si {@code montant > 0} ET {@code montant <= solde} ;
     * sinon refuse (objet inchange, jamais de solde negatif).
     */
    public void retirer(double montant) {
        // TODO
    }

    /**
     * @return une chaine de la forme {@code "Compte de <titulaire> : <solde> €"}
     *         (solde a deux decimales, point decimal, {@code Locale.ROOT}).
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
