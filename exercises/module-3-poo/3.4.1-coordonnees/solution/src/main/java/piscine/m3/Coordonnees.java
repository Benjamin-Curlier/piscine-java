package piscine.m3;

/**
 * Exercice 3.4.1 — Coordonnees (solution de reference) : un record immuable.
 *
 * <p>Les accesseurs x() et y() et l'equals sont generes. translater renvoie un
 * NOUVEAU record (immuabilite).</p>
 */
public record Coordonnees(int x, int y) {

    public int normeCarree() {
        return x * x + y * y;
    }

    public Coordonnees translater(int dx, int dy) {
        return new Coordonnees(x + dx, y + dy);
    }
}
