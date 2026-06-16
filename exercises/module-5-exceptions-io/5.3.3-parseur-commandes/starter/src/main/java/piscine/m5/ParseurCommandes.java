package piscine.m5;

/**
 * Parseur de commandes texte.
 * Interprète une ligne de la forme "commande arg1 arg2" et renvoie le résultat entier.
 */
public class ParseurCommandes {

    /**
     * Exécute la commande décrite dans {@code ligne} et renvoie le résultat.
     *
     * <p>Commandes supportées :
     * <ul>
     *   <li>{@code somme a b}   — renvoie a + b</li>
     *   <li>{@code diff a b}    — renvoie a - b</li>
     *   <li>{@code produit a b} — renvoie a * b</li>
     *   <li>{@code oppose a}    — renvoie -a</li>
     * </ul>
     *
     * @param ligne la ligne de commande à interpréter (peut contenir des espaces multiples)
     * @return le résultat entier du calcul
     * @throws IllegalArgumentException si la ligne est vide/blanche, la commande inconnue,
     *                                  ou le nombre d'arguments incorrect
     * @throws NumberFormatException    si un argument ne peut pas être converti en entier
     */
    public static int executer(String ligne) {
        // TODO : implémenter le parseur
        return 0;
    }
}
