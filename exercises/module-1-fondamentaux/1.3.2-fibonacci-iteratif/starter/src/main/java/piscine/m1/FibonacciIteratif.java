package piscine.m1;

import java.util.Scanner;

/**
 * Exercice 1.3.2 — Fibonacci iteratif.
 *
 * Completez la methode main pour lire un rang n et afficher le n-ieme terme
 * de la suite de Fibonacci, calcule de maniere ITERATIVE (avec une boucle).
 *
 * Rappel : F(0) = 0, F(1) = 1, puis chaque terme est la somme des deux precedents
 * (0, 1, 1, 2, 3, 5, 8, 13, ...). N'utilisez pas la recursivite (vue plus tard).
 *
 * Astuce : gardez deux variables de type long (le precedent et le courant) et
 * faites-les "glisser" a chaque tour.
 *
 * Ne modifiez ni le nom de la classe, ni le package.
 */
public class FibonacciIteratif {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel rang de la suite de Fibonacci ?");
        // TODO : lire le rang n (un entier)
        // TODO : calculer F(n) de maniere iterative (deux variables long qui glissent)
        // TODO : afficher "F(<n>) = <valeur>"
    }
}
