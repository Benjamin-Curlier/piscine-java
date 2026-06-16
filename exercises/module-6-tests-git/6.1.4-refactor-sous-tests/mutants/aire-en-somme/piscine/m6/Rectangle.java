package piscine.m6;

// MUTANT « aire-en-somme » : aire renvoie largeur + hauteur au lieu de largeur × hauteur.
// Tué par un test affirmant Rectangle(3, 4).aire() == 12 (le mutant donne 7).
public class Rectangle {

    private final int largeur;
    private final int hauteur;

    public Rectangle(int largeur, int hauteur) {
        this.largeur = largeur > 0 ? largeur : 1;
        this.hauteur = hauteur > 0 ? hauteur : 1;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int aire() {
        return largeur + hauteur;
    }

    public int perimetre() {
        return 2 * (largeur + hauteur);
    }
}
