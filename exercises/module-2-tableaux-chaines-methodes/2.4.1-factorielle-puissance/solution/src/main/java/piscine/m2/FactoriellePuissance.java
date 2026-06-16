package piscine.m2;

/**
 * Exercice 2.4.1 — Factorielle et puissance (solution de reference, recursive).
 *
 * <p>Chaque methode a un cas de base (qui arrete la recursion) et un cas
 * recursif (appel sur un probleme plus petit). Le type {@code long} evite le
 * debordement : 13! depasse deja la capacite d'un int.</p>
 */
public class FactoriellePuissance {

    /** n! (n >= 0). Recursif. */
    public static long factorielle(int n) {
        if (n <= 1) {
            return 1;                         // cas de base : 0! = 1! = 1
        }
        return n * factorielle(n - 1);        // cas recursif : on reduit n vers 1
    }

    /** base^exposant (exposant >= 0). Recursif. */
    public static long puissance(int base, int exposant) {
        if (exposant == 0) {
            return 1;                         // cas de base : base^0 = 1
        }
        return base * puissance(base, exposant - 1); // cas recursif : on reduit l'exposant
    }
}
