package etnc.m3;

/**
 * Exercice 3.3.4 — Parc (solution de reference) : operations polymorphes sur
 * un tableau de Forme.
 */
public class Parc {

    /** Somme des aires de toutes les formes. */
    public static double surfaceTotale(Forme[] formes) {
        double total = 0;
        for (Forme f : formes) {
            total += f.aire();
        }
        return total;
    }

    /** Forme d'aire maximale, ou null si le tableau est vide. */
    public static Forme plusGrande(Forme[] formes) {
        Forme max = null;
        for (Forme f : formes) {
            if (max == null || f.aire() > max.aire()) {
                max = f;
            }
        }
        return max;
    }

    /** Nombre de formes qui sont des cercles. */
    public static int compterCercles(Forme[] formes) {
        int compte = 0;
        for (Forme f : formes) {
            if (f instanceof Cercle) {
                compte++;
            }
        }
        return compte;
    }
}
