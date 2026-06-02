package etnc.m2;

/**
 * Exercice 2.3.3 — Calculs geometriques (solution de reference).
 *
 * <p>Refactor de l'exercice 1.2.2 : les calculs du cercle sont extraits en
 * methodes static, et on ajoute une surcharge pour le rectangle. Une version a
 * un parametre concerne le cercle, une version a deux parametres le rectangle
 * (surcharge par nombre de parametres).</p>
 */
public class CalculsGeometriques {

    /** Aire d'un cercle de rayon donne. */
    public static double aire(double rayon) {
        return Math.PI * rayon * rayon;
    }

    /** Aire d'un rectangle de largeur et hauteur donnees. */
    public static double aire(double largeur, double hauteur) {
        return largeur * hauteur;
    }

    /** Perimetre d'un cercle de rayon donne. */
    public static double perimetre(double rayon) {
        return 2 * Math.PI * rayon;
    }

    /** Perimetre d'un rectangle de largeur et hauteur donnees. */
    public static double perimetre(double largeur, double hauteur) {
        return 2 * (largeur + hauteur);
    }
}
