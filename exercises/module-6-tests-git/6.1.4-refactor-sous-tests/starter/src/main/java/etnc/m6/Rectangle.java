package etnc.m6;

/**
 * Classe FOURNIE et CORRECTE (issue du module 3, avec invariant). Ne la modifiez pas :
 * écrivez le filet de tests qui la sécurise (voir {@code src/test/java/etnc/m6/RectangleTest.java}).
 */
public class Rectangle {

    private final int largeur;
    private final int hauteur;

    /** Une dimension <= 0 est corrigée à 1 (invariant : pas de rectangle dégénéré). */
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
        return 2 * (largeur + hauteur);
    }
}
