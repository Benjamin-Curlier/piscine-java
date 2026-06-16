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
        // Garde : rejeter explicitement une entrée vide ou blanche avant tout traitement
        if (ligne == null || ligne.isBlank()) {
            throw new IllegalArgumentException("commande vide");
        }

        // trim() absorbe les espaces en tête/queue ;
        // split("\\s+") découpe sur un ou plusieurs espaces → tokens propres
        String[] tokens = ligne.trim().split("\\s+");
        String cmd = tokens[0];

        // Dispatch sur le nom de la commande — switch expressif, un cas par opération
        switch (cmd) {
            case "somme" -> {
                // Valider le nombre d'arguments AVANT Integer.parseInt pour un message clair
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("arguments invalides pour " + cmd);
                }
                int a = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);
                return a + b;
            }
            case "diff" -> {
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("arguments invalides pour " + cmd);
                }
                int a = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);
                // diff est non commutatif : a - b, pas b - a
                return a - b;
            }
            case "produit" -> {
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("arguments invalides pour " + cmd);
                }
                int a = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);
                return a * b;
            }
            case "oppose" -> {
                if (tokens.length != 2) {
                    throw new IllegalArgumentException("arguments invalides pour " + cmd);
                }
                // NumberFormatException de parseInt se propage naturellement à l'appelant
                return -Integer.parseInt(tokens[1]);
            }
            default -> throw new IllegalArgumentException("commande inconnue : " + cmd);
        }
    }
}
