package piscine.m5;

/**
 * Un membre du personnel : son nom, son niveau et son ancienneté (en années).
 *
 * FOURNI — ne pas modifier. Record : equals/hashCode/toString sont générés
 * automatiquement, ce qui permet de comparer deux Personnel par leur contenu
 * (utile pour les tests : containsExactly compare champ par champ).
 *
 * @param nom        nom du membre du personnel
 * @param niveau      niveau de séniorité
 * @param anciennete ancienneté en années (peut être négative : on lit ce qui
 *                   est écrit dans le fichier, sans contrôle métier ici)
 */
public record Personnel(String nom, Niveau niveau, int anciennete) {
}
