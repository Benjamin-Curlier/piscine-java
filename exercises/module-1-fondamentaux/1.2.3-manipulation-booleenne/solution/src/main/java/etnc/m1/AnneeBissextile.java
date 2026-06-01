package etnc.m1;

import java.util.Scanner;

/**
 * Exercice 1.2.3 — Manipulation booleenne (solution de reference).
 *
 * <p>Determine si une annee est bissextile en combinant le modulo et les
 * operateurs logiques. Le resultat est d'abord range dans un {@code boolean}
 * nomme, ce qui rend la condition d'affichage tres lisible.</p>
 */
public class AnneeBissextile {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quelle année ?");
        int annee = clavier.nextInt();

        // Divisible par 4 mais pas par 100, OU divisible par 400.
        boolean estBissextile = (annee % 4 == 0 && annee % 100 != 0) || annee % 400 == 0;

        if (estBissextile) {
            System.out.println(annee + " est bissextile.");
        } else {
            System.out.println(annee + " n'est pas bissextile.");
        }
    }
}
