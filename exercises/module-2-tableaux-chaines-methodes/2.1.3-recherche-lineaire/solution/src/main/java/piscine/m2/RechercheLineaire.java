package piscine.m2;

import java.util.Scanner;

/**
 * Exercice 2.1.3 — Recherche lineaire (solution de reference).
 *
 * <p>On parcourt le tableau du debut a la fin et on s'arrete des la premiere
 * case egale a la valeur cherchee. La variable {@code indice} vaut -1 tant que
 * rien n'a ete trouve, ce qui distingue le cas "absent".</p>
 */
public class RechercheLineaire {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int n = clavier.nextInt();

        int[] valeurs = new int[n];
        for (int i = 0; i < n; i++) {
            valeurs[i] = clavier.nextInt();
        }
        int cible = clavier.nextInt();

        int indice = -1;
        for (int i = 0; i < n; i++) {
            if (valeurs[i] == cible) {
                indice = i;
                break;
            }
        }

        if (indice >= 0) {
            System.out.println("Indice : " + indice);
        } else {
            System.out.println("Absent");
        }
    }
}
