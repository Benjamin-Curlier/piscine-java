package etnc.m2;

import java.util.Scanner;

/**
 * Exercice 2.1.4 — Rotation d'une matrice carree (solution de reference).
 *
 * <p>Rotation de 90 degres dans le sens horaire. La ligne {@code i} de la
 * sortie correspond a la colonne {@code i} de la source, lue de bas en haut :
 * {@code resultat[i][j] = source[n-1-j][i]}. On construit toute la sortie dans
 * un StringBuilder pour eviter les espaces finaux superflus.</p>
 */
public class RotationMatrice {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int n = clavier.nextInt();

        int[][] matrice = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = clavier.nextInt();
            }
        }

        StringBuilder sortie = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sortie.append(matrice[n - 1 - j][i]);
                if (j < n - 1) {
                    sortie.append(' ');
                }
            }
            sortie.append(System.lineSeparator());
        }
        System.out.print(sortie);
    }
}
