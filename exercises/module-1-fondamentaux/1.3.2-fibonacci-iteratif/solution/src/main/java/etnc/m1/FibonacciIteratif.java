package etnc.m1;

import java.util.Scanner;

/**
 * Exercice 1.3.2 — Fibonacci iteratif (solution de reference).
 *
 * <p>Calcule F(n) avec une boucle et deux variables qui "glissent".
 * On utilise {@code long} car les termes de Fibonacci grandissent vite et
 * depasseraient la capacite d'un {@code int} pour des rangs eleves.</p>
 */
public class FibonacciIteratif {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel rang de la suite de Fibonacci ?");
        int n = clavier.nextInt();

        // a vaut F(i), b vaut F(i+1). Apres n tours, a vaut F(n).
        long a = 0;
        long b = 1;
        for (int i = 0; i < n; i++) {
            long suivant = a + b;
            a = b;
            b = suivant;
        }

        System.out.println("F(" + n + ") = " + a);
    }
}
