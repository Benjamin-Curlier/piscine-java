package etnc.m5;

/**
 * Exercice 5.1.3 — Calculs géométriques avec gardes (solution de référence).
 *
 * <p>Refactor de l'exercice 2.3.3 : on ajoute des gardes défensives sur les
 * dimensions négatives. Le zéro est une valeur dégénérée mais autorisée
 * (aire nulle, périmètre nul).</p>
 *
 * <p>Le helper privé {@link #exigerPositif} mutualise la garde pour éviter
 * la duplication (critère idiomatisme) : un seul endroit où la règle est
 * écrite, un seul endroit à modifier si elle évolue.</p>
 */
public class CalculsGeometriques {

    /**
     * Vérifie qu'une dimension est >= 0 ; lève une {@link IllegalArgumentException}
     * sinon, en nommant le paramètre fautif dans le message.
     *
     * @param valeur la dimension à contrôler
     * @param nom    le nom du paramètre (ex. "rayon", "largeur", "hauteur")
     * @throws IllegalArgumentException si valeur < 0
     */
    private static void exigerPositif(double valeur, String nom) {
        // On utilise la comparaison stricte : 0.0 et -0.0 ne déclenchent pas
        // la garde (-0.0 < 0 est false en Java), ce qui est le comportement voulu.
        if (valeur < 0) {
            throw new IllegalArgumentException(
                "La dimension '" + nom + "' doit être >= 0, valeur reçue : " + valeur);
        }
    }

    /** Aire d'un cercle de rayon donné. */
    public static double aire(double rayon) {
        exigerPositif(rayon, "rayon");
        return Math.PI * rayon * rayon;
    }

    /** Aire d'un rectangle de largeur et hauteur données. */
    public static double aire(double largeur, double hauteur) {
        exigerPositif(largeur, "largeur");
        exigerPositif(hauteur, "hauteur");
        return largeur * hauteur;
    }

    /** Périmètre d'un cercle de rayon donné. */
    public static double perimetre(double rayon) {
        exigerPositif(rayon, "rayon");
        return 2 * Math.PI * rayon;
    }

    /** Périmètre d'un rectangle de largeur et hauteur données. */
    public static double perimetre(double largeur, double hauteur) {
        exigerPositif(largeur, "largeur");
        exigerPositif(hauteur, "hauteur");
        return 2 * (largeur + hauteur);
    }
}
