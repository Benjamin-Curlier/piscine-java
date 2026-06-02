package etnc.m2;

/**
 * Exercice 2.3.2 — Surcharge de methodes (solution de reference).
 *
 * <p>Trois methodes de meme nom, distinguees par leurs parametres : deux puis
 * trois entiers (surcharge par nombre), et deux double (surcharge par type).
 * On reutilise {@code Math.max} pour rester lisible ; la version a trois
 * entiers s'appuie sur celle a deux.</p>
 */
public class Surcharge {

    /** Le plus grand de deux entiers. */
    public static int maximum(int a, int b) {
        return Math.max(a, b);
    }

    /** Le plus grand de trois entiers. */
    public static int maximum(int a, int b, int c) {
        return maximum(maximum(a, b), c);
    }

    /** Le plus grand de deux nombres a virgule. */
    public static double maximum(double a, double b) {
        return Math.max(a, b);
    }
}
