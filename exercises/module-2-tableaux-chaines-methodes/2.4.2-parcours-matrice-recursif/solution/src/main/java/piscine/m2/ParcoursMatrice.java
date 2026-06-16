package piscine.m2;

/**
 * Exercice 2.4.2 — Parcours recursif de matrice (solution de reference).
 *
 * <p>Deux niveaux de recursion : on somme une ligne (recursif sur les colonnes),
 * et on somme la matrice (recursif sur les lignes). Le cas de base est atteint
 * lorsqu'il n'y a plus de ligne (resp. plus de colonne) a traiter.</p>
 */
public class ParcoursMatrice {

    /** Somme de tous les elements de la matrice (recursif sur les lignes). */
    public static int sommeMatrice(int[][] matrice) {
        return sommeDepuisLigne(matrice, 0);
    }

    /** Somme des lignes a partir de l'indice {@code i} (cas de base : i == nombre de lignes). */
    private static int sommeDepuisLigne(int[][] matrice, int i) {
        if (i == matrice.length) {
            return 0;
        }
        return sommeLigne(matrice[i], 0) + sommeDepuisLigne(matrice, i + 1);
    }

    /** Somme des elements d'une ligne a partir de l'indice {@code j} (cas de base : j == longueur). */
    private static int sommeLigne(int[] ligne, int j) {
        if (j == ligne.length) {
            return 0;
        }
        return ligne[j] + sommeLigne(ligne, j + 1);
    }
}
