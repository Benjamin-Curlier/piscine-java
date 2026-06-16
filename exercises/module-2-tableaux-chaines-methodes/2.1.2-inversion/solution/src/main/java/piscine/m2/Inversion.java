package piscine.m2;

import java.util.Scanner;

/**
 * Exercice 2.1.2 — Inversion d'un tableau (solution de reference).
 *
 * <p>On lit le tableau puis on le parcourt de la derniere case vers la premiere
 * en construisant la ligne de sortie. Un StringBuilder evite d'afficher un
 * espace final superflu.</p>
 */
public class Inversion {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int n = clavier.nextInt();

        int[] valeurs = new int[n];
        for (int i = 0; i < n; i++) {
            valeurs[i] = clavier.nextInt();
        }

        StringBuilder ligne = new StringBuilder();
        for (int i = n - 1; i >= 0; i--) {
            ligne.append(valeurs[i]);
            if (i > 0) {
                ligne.append(' ');
            }
        }
        System.out.println(ligne);
    }
}
