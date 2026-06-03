package etnc.m3;

import java.util.Locale;

/**
 * Exercice 3.1.1 — Point2D (solution de reference).
 *
 * <p>Un point du plan a coordonnees reelles. Illustre une classe minimale :
 * attributs prives, accesseurs, une methode mutatrice ({@link #deplacer}),
 * une methode derivee ({@link #distance}) et un {@code toString} formate.</p>
 */
public class Point2D {

    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /** Translate le point de {@code dx} en abscisse et {@code dy} en ordonnee. */
    public void deplacer(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /** @return la distance euclidienne entre ce point et {@code autre}. */
    public double distance(Point2D autre) {
        double dx = this.x - autre.x;
        double dy = this.y - autre.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(%s, %s)", x, y);
    }
}
