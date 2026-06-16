package piscine.m2;

import java.util.Scanner;

/**
 * Exercice 2.2.2 — Comptage d'occurrences (solution de reference).
 *
 * <p>On lit le texte puis le caractere cherche, et on parcourt le texte
 * caractere par caractere en incrementant un compteur a chaque correspondance
 * exacte (la casse compte).</p>
 */
public class ComptageOccurrences {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        String texte = clavier.nextLine();
        char cible = clavier.nextLine().charAt(0);

        int compte = 0;
        for (int i = 0; i < texte.length(); i++) {
            if (texte.charAt(i) == cible) {
                compte++;
            }
        }

        System.out.println("Occurrences : " + compte);
    }
}
