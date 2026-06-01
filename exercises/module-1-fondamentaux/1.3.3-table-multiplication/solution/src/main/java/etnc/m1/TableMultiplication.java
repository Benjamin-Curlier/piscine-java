package etnc.m1;

import java.util.Scanner;

/**
 * Exercice 1.3.3 — Table de multiplication (solution de reference).
 *
 * <p>Affiche la table de n de 1 a 10 avec une boucle for : le nombre de tours
 * (10) est connu d'avance, c'est le cas typique du for.</p>
 */
public class TableMultiplication {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quelle table ?");
        int n = clavier.nextInt();

        for (int i = 1; i <= 10; i++) {
            System.out.println(n + " x " + i + " = " + (n * i));
        }
    }
}
