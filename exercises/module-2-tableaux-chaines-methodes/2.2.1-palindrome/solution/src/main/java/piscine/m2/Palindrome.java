package piscine.m2;

import java.util.Scanner;

/**
 * Exercice 2.2.1 — Palindrome (solution de reference).
 *
 * <p>On normalise la casse, puis on compare les caracteres deux a deux depuis
 * les deux extremites vers le centre. Des qu'une paire differe, ce n'est pas
 * un palindrome.</p>
 */
public class Palindrome {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        String mot = clavier.nextLine().strip().toLowerCase();

        boolean palindrome = true;
        for (int i = 0; i < mot.length() / 2; i++) {
            if (mot.charAt(i) != mot.charAt(mot.length() - 1 - i)) {
                palindrome = false;
                break;
            }
        }

        System.out.println(palindrome ? "oui" : "non");
    }
}
