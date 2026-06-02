package etnc.m2;

/**
 * Exercice 2.4.1 — Factorielle et puissance (versions RECURSIVES).
 *
 * Completez les deux methodes static. Elles doivent etre RECURSIVES : un cas
 * de base, puis un appel a elles-memes sur un probleme plus petit. Testees par
 * valeur de retour.
 */
public class FactoriellePuissance {

    /** n! (n >= 0). Recursif. */
    public static long factorielle(int n) {
        // TODO (recursif) : cas de base n <= 1 -> 1 ; sinon n * factorielle(n - 1)
        return 0L;
    }

    /** base^exposant (exposant >= 0). Recursif. */
    public static long puissance(int base, int exposant) {
        // TODO (recursif) : cas de base exposant == 0 -> 1 ; sinon base * puissance(base, exposant - 1)
        return 0L;
    }
}
