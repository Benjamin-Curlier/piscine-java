package etnc.m3;

/**
 * Exercice 3.4.1 — Coordonnees : un record immuable (en-tete FOURNI).
 *
 * <p>Les accesseurs x() et y() et l'equals sont generes automatiquement.
 * Completez les deux methodes ci-dessous sans changer leurs signatures.</p>
 */
public record Coordonnees(int x, int y) {

    /** @return x*x + y*y. */
    public int normeCarree() {
        return 0; // TODO
    }

    /** @return un NOUVEAU Coordonnees decale de (dx, dy) — l'original est inchange. */
    public Coordonnees translater(int dx, int dy) {
        return null; // TODO
    }
}
