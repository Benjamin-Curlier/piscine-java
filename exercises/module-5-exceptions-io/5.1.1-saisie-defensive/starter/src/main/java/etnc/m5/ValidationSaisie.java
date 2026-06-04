package etnc.m5;

/**
 * Utilitaire de validation défensive d'une saisie entière.
 *
 * Fournit une méthode statique pour convertir et valider un texte en entier
 * borné, en distinguant les deux familles d'erreur possibles :
 * - entrée illisible (non numérique, overflow) → NumberFormatException
 * - entrée hors de la plage autorisée → IllegalArgumentException
 */
public class ValidationSaisie {

    /**
     * Valide qu'un texte représente un entier compris dans [min, max].
     *
     * @param texte le texte à convertir (peut être null)
     * @param min   borne inférieure inclusive
     * @param max   borne supérieure inclusive
     * @return l'entier converti si tout est valide
     * @throws NumberFormatException    si texte est null, non numérique ou provoque un overflow
     * @throws IllegalArgumentException si la valeur est hors de [min, max]
     */
    public static int validerEntier(String texte, int min, int max) {
        return 0; // TODO
    }
}
