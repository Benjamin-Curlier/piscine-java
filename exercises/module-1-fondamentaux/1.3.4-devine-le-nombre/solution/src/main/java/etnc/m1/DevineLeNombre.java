package etnc.m1;

import java.util.Random;
import java.util.Scanner;

/**
 * Exercice 1.3.4 — Devine le nombre (solution de reference).
 *
 * <p>Le secret est tire avec une graine fixe : le jeu reste reproductible
 * (utile pour la correction automatique). Le cœur de l'exercice est la
 * boucle {@code while} qui tourne jusqu'a ce que le joueur trouve.</p>
 */
public class DevineLeNombre {

    public static void main(String[] args) {
        final int GRAINE = 1789;                            // NE PAS modifier
        int secret = new Random(GRAINE).nextInt(100) + 1;   // nombre a deviner, entre 1 et 100

        Scanner clavier = new Scanner(System.in);
        System.out.println("Devinez le nombre (entre 1 et 100) :");

        int essais = 0;
        boolean trouve = false;
        while (!trouve) {
            int essai = clavier.nextInt();
            essais++;

            if (essai < secret) {
                System.out.println("C'est plus grand.");
            } else if (essai > secret) {
                System.out.println("C'est plus petit.");
            } else {
                // L'essai vaut le secret : on annonce la victoire et on arrete la boucle.
                System.out.println("Bravo, vous avez trouvé en " + essais + " essai(s) !");
                trouve = true;
            }
        }
    }
}
