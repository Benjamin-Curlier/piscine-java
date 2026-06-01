package etnc.m1;

import java.util.Random;
import java.util.Scanner;

/**
 * Exercice 1.3.4 — Devine le nombre.
 *
 * Le nombre secret est DEJA tire pour vous ci-dessous (variable secret).
 * Votre travail : ecrire la boucle de jeu.
 *
 * Tant que le joueur n'a pas trouve, lisez un essai au clavier et affichez :
 *   - "C'est plus grand." si l'essai est INFERIEUR au secret,
 *   - "C'est plus petit." si l'essai est SUPERIEUR au secret,
 *   - "Bravo, vous avez trouvé en N essai(s) !" quand l'essai vaut le secret
 *     (N = nombre d'essais effectues), puis arretez la boucle.
 *
 * Ne modifiez NI la graine, NI la ligne du secret, ni le nom de la classe.
 */
public class DevineLeNombre {

    public static void main(String[] args) {
        final int GRAINE = 1789;                            // NE PAS modifier
        int secret = new Random(GRAINE).nextInt(100) + 1;   // nombre a deviner, entre 1 et 100

        Scanner clavier = new Scanner(System.in);
        System.out.println("Devinez le nombre (entre 1 et 100) :");

        // TODO : ecrire la boucle de jeu (voir la description ci-dessus).
    }
}
