package piscine.m3;

/**
 * Exercice 3.2.3 — Date simple (solution de reference).
 *
 * <p>Le constructeur borne le jour a [1..31] et le mois a [1..12] ; les setters
 * refusent une valeur hors plage (objet inchange). Date simple : pas de nombre
 * de jours par mois ni d'annee bissextile.</p>
 */
public class Date {

    private int jour;
    private int mois;
    private int annee;

    public Date(int jour, int mois, int annee) {
        this.jour = borner(jour, 1, 31);
        this.mois = borner(mois, 1, 12);
        this.annee = annee;
    }

    private static int borner(int valeur, int min, int max) {
        if (valeur < min) {
            return min;
        }
        if (valeur > max) {
            return max;
        }
        return valeur;
    }

    public int getJour() {
        return jour;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setJour(int jour) {
        if (jour >= 1 && jour <= 31) {
            this.jour = jour;
        }
    }

    public void setMois(int mois) {
        if (mois >= 1 && mois <= 12) {
            this.mois = mois;
        }
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", jour, mois, annee);
    }
}
