package piscine.m3;

/**
 * Exercice 3.2.3 — Date simple : bornage des champs.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus. Aucune exception : une valeur hors plage est
 * bornee (constructeur) ou ignoree (setter).</p>
 */
public class Date {

    private int jour;
    private int mois;
    private int annee;

    /**
     * Construit une date. Le jour est borne a l'intervalle [1..31], le mois a
     * [1..12] (une valeur hors plage est ramenee a la borne la plus proche).
     * L'annee n'est pas contrainte.
     */
    public Date(int jour, int mois, int annee) {
        // TODO : affecter annee, et jour/mois en les bornant a [1..31] / [1..12]
    }

    /** @return le jour. */
    public int getJour() {
        return 0; // TODO
    }

    /** @return le mois. */
    public int getMois() {
        return 0; // TODO
    }

    /** @return l'annee. */
    public int getAnnee() {
        return 0; // TODO
    }

    /** Modifie le jour s'il est dans [1..31] ; sinon ne change rien. */
    public void setJour(int jour) {
        // TODO
    }

    /** Modifie le mois s'il est dans [1..12] ; sinon ne change rien. */
    public void setMois(int mois) {
        // TODO
    }

    /**
     * @return une chaine au format {@code "JJ/MM/AAAA"} avec zero-padding,
     *         ex. {@code "07/03/2026"} (astuce : {@code String.format("%02d/%02d/%04d", ...)}).
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
