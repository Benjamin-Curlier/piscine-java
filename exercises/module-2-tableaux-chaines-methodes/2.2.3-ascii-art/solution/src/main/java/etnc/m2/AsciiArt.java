package etnc.m2;

import java.util.Scanner;

/**
 * Exercice 2.2.3 — ASCII art : triangle d'etoiles (solution de reference).
 *
 * <p>Double boucle : la boucle externe parcourt les lignes (de 1 a n), la boucle
 * interne ajoute autant d'etoiles que le numero de ligne. On construit toute la
 * sortie dans un StringBuilder avant de l'afficher.</p>
 */
public class AsciiArt {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        int n = clavier.nextInt();

        StringBuilder sortie = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                sortie.append('*');
            }
            sortie.append(System.lineSeparator());
        }
        System.out.print(sortie);
    }
}
