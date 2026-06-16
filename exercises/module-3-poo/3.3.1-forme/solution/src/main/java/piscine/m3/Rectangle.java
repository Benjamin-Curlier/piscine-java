package piscine.m3;

/** Exercice 3.3.1 — Rectangle (solution de reference). */
public class Rectangle extends Forme {

    private double largeur;
    private double hauteur;

    public Rectangle(double largeur, double hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    @Override
    public double aire() {
        return largeur * hauteur;
    }

    @Override
    public double perimetre() {
        return 2 * (largeur + hauteur);
    }
}
