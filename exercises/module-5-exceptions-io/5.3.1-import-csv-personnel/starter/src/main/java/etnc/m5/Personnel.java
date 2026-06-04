package etnc.m5;

/**
 * Un membre du personnel : son nom, son grade et son ancienneté (en années).
 *
 * FOURNI — ne pas modifier. Record : equals/hashCode/toString sont générés
 * automatiquement, ce qui permet de comparer deux Personnel par leur contenu
 * (utile pour les tests : containsExactly compare champ par champ).
 *
 * @param nom        nom du membre du personnel
 * @param grade      grade militaire
 * @param anciennete ancienneté en années (peut être négative : on lit ce qui
 *                   est écrit dans le fichier, sans contrôle métier ici)
 */
public record Personnel(String nom, Grade grade, int anciennete) {
}
