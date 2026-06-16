package piscine.m5;

/**
 * Utilitaire de validation défensive d'une saisie entière.
 *
 * Fournit une méthode statique pour convertir et valider un texte en entier
 * borné, en distinguant les deux familles d'erreur possibles :
 * - entrée illisible (null, non numérique, overflow) → NumberFormatException
 * - entrée hors de la plage autorisée → IllegalArgumentException
 *
 * Note : NumberFormatException extends IllegalArgumentException, mais les deux
 * levées sont volontairement distinctes pour que l'appelant puisse choisir de
 * traiter l'une sans attraper l'autre.
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
        // Étape 1 : garde null explicite — texte.trim() lèverait une NullPointerException
        // sans message utile ; on regroupe null avec les entrées illisibles.
        if (texte == null) {
            throw new NumberFormatException("Le texte ne peut pas être null");
        }

        // Étape 2 : conversion — on laisse remonter la NumberFormatException de parseInt
        // si le texte est non numérique ou dépasse les bornes de int (overflow).
        int valeur = Integer.parseInt(texte.trim());

        // Étape 3 : vérification de la plage — on lève notre propre IAE (pas une NFE)
        // pour signaler clairement que le format était correct mais la valeur inadmissible.
        if (valeur < min || valeur > max) {
            throw new IllegalArgumentException(
                "La valeur " + valeur + " est hors de la plage autorisée [" + min + ", " + max + "]"
            );
        }

        // Étape 4 : tout est valide, on renvoie la valeur convertie.
        return valeur;
    }
}
