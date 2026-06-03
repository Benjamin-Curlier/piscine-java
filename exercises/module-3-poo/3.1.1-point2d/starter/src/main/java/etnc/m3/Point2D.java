package etnc.m3;

/**
 * Exercice 3.1.1 — Point2D : un point du plan a coordonnees reelles.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus.</p>
 */
public class Point2D {

    private double x;
    private double y;

    /** Construit un point de coordonnees {@code (x, y)}. */
    public Point2D(double x, double y) {
        // TODO : initialiser les attributs x et y
    }

    /** @return l'abscisse du point. */
    public double getX() {
        return 0; // TODO
    }

    /** @return l'ordonnee du point. */
    public double getY() {
        return 0; // TODO
    }

    /** Translate le point de {@code dx} en abscisse et {@code dy} en ordonnee. */
    public void deplacer(double dx, double dy) {
        // TODO : mettre a jour x et y
    }

    /**
     * @return la distance euclidienne entre ce point et {@code autre},
     *         c.-a-d. la racine carree de (dx*dx + dy*dy) (voir {@link Math#sqrt}).
     */
    public double distance(Point2D autre) {
        return 0; // TODO
    }

    /**
     * @return une representation de la forme {@code "(x, y)"}, ex. {@code "(1.0, 2.0)"}.
     *         Utilisez {@code java.util.Locale.ROOT} pour un formatage stable.
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
