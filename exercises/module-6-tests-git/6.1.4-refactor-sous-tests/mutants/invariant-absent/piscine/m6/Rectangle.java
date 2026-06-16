package piscine.m6;

// MUTANT « invariant-absent » : le constructeur ne corrige plus les dimensions invalides.
// Tué par un test affirmant que new Rectangle(-5, 4).getLargeur() == 1 (le mutant donne -5).
public class Rectangle {

    private final int largeur;
    private final int hauteur;

    public Rectangle(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int aire() {
        return largeur * hauteur;
    }

    public int perimetre() {
        return 2 * (largeur + hauteur);
    }
}
