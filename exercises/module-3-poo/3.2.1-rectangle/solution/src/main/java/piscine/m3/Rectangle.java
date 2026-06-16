package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.2.1 — Rectangle (solution de reference).
 *
 * <p>Invariants : largeur et hauteur strictement positives. Le constructeur
 * corrige toute dimension {@code <= 0} a {@code 1.0} ; les setters refusent une
 * valeur invalide en laissant l'objet inchange. Le constructeur carre chaine
 * via {@code this(cote, cote)}.</p>
 */
public class Rectangle {

    private double largeur;
    private double hauteur;

    public Rectangle(double largeur, double hauteur) {
        this.largeur = largeur > 0 ? largeur : 1.0;
        this.hauteur = hauteur > 0 ? hauteur : 1.0;
    }

    public Rectangle(double cote) {
        this(cote, cote);
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setLargeur(double largeur) {
        if (largeur > 0) {
            this.largeur = largeur;
        }
    }

    public void setHauteur(double hauteur) {
        if (hauteur > 0) {
            this.hauteur = hauteur;
        }
    }

    public double aire() {
        return largeur * hauteur;
    }

    public double perimetre() {
        return 2 * (largeur + hauteur);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Rectangle %s x %s", largeur, hauteur);
    }
}
