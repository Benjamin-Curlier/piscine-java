package piscine.m3;

/**
 * Exercice 3.2.1 — Rectangle : invariants, validation et surcharge de constructeurs.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus.</p>
 */
public class Rectangle {

    private double largeur;
    private double hauteur;

    /**
     * Construit un rectangle. Invariant : largeur et hauteur strictement positives.
     * Toute dimension {@code <= 0} est corrigee a {@code 1.0}.
     */
    public Rectangle(double largeur, double hauteur) {
        // TODO : affecter largeur/hauteur en corrigeant toute valeur <= 0 a 1.0
    }

    /**
     * Construit un carre de cote {@code cote}.
     * Doit CHAINER vers l'autre constructeur via {@code this(cote, cote)}.
     */
    public Rectangle(double cote) {
        // TODO : chainer vers Rectangle(double, double)
    }

    /** @return la largeur. */
    public double getLargeur() {
        return 0; // TODO
    }

    /** @return la hauteur. */
    public double getHauteur() {
        return 0; // TODO
    }

    /** Modifie la largeur si elle est strictement positive ; sinon ne change rien. */
    public void setLargeur(double largeur) {
        // TODO
    }

    /** Modifie la hauteur si elle est strictement positive ; sinon ne change rien. */
    public void setHauteur(double hauteur) {
        // TODO
    }

    /** @return l'aire (largeur * hauteur). */
    public double aire() {
        return 0; // TODO
    }

    /** @return le perimetre (2 * (largeur + hauteur)). */
    public double perimetre() {
        return 0; // TODO
    }

    /**
     * @return une chaine de la forme {@code "Rectangle <largeur> x <hauteur>"},
     *         ex. {@code "Rectangle 3.0 x 4.0"}. Utilisez {@code java.util.Locale.ROOT}.
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
