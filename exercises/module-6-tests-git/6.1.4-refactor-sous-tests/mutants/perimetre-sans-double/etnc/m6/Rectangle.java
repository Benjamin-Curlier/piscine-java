package etnc.m6;

// MUTANT « perimetre-sans-double » : périmètre oublie le facteur 2.
// Tué par un test affirmant Rectangle(3, 4).perimetre() == 14 (le mutant donne 7).
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
        return largeur * hauteur;
    }

    public int perimetre() {
        return largeur + hauteur;
    }
}
