package piscine.m2;

/**
 * Exercice 2.3.1 — Bibliotheque mathematique (solution de reference).
 *
 * <p>Trois methodes static independantes, testees par leur valeur de retour.</p>
 */
public class BiblioMaths {

    /** Plus grand commun diviseur de a et b (entiers >= 0), par l'algorithme d'Euclide. */
    public static int pgcd(int a, int b) {
        // Tant que b n'est pas nul, on remplace (a, b) par (b, a % b).
        // Quand b atteint 0, a contient le PGCD.
        while (b != 0) {
            int reste = a % b;
            a = b;
            b = reste;
        }
        return a;
    }

    /** Vrai si n est un nombre premier (n < 2 -> false). */
    public static boolean estPremier(int n) {
        if (n < 2) {
            return false;
        }
        // Il suffit de tester les diviseurs jusqu'a la racine carree de n.
        for (int d = 2; (long) d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }

    /** Somme des chiffres de n (n >= 0). */
    public static int sommeChiffres(int n) {
        int somme = 0;
        // On detache le dernier chiffre (n % 10) puis on le retire (n / 10).
        while (n > 0) {
            somme += n % 10;
            n /= 10;
        }
        return somme;
    }
}
