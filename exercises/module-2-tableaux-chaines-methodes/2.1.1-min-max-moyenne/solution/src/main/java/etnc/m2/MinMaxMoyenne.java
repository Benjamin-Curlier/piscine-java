package etnc.m2;

import java.util.Locale;
import java.util.Scanner;

/**
 * Exercice 2.1.1 — Min, max et moyenne (solution de reference).
 *
 * <p>Lit une taille puis autant d'entiers, et affiche le minimum, le maximum
 * et la moyenne. La somme est cumulee dans un {@code long} pour eviter tout
 * debordement sur de grands tableaux ; la moyenne est formatee avec un point
 * decimal (Locale.ROOT) afin que la sortie soit independante de la locale.</p>
 */
public class MinMaxMoyenne {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int n = clavier.nextInt();

        int[] valeurs = new int[n];
        for (int i = 0; i < n; i++) {
            valeurs[i] = clavier.nextInt();
        }

        int min = valeurs[0];
        int max = valeurs[0];
        long somme = 0;
        for (int v : valeurs) {
            if (v < min) {
                min = v;
            }
            if (v > max) {
                max = v;
            }
            somme += v;
        }
        double moyenne = (double) somme / n;

        System.out.println("Min : " + min);
        System.out.println("Max : " + max);
        System.out.println("Moyenne : " + String.format(Locale.ROOT, "%.2f", moyenne));
    }
}
