package piscine.m1;

import java.util.Scanner;

/**
 * Exercice 1.1.3 — Lecture d'une saisie.
 *
 * Completez la methode main pour lire un prenom puis un age au clavier,
 * et afficher le message demande dans le sujet.
 *
 * Astuce : lisez le prenom (une ligne entiere) AVANT l'age. Lire une ligne
 * de texte avant un nombre evite le piege classique du tampon
 * (voir le chapitre 1.4).
 *
 * Ne modifiez ni le nom de la classe, ni le package.
 */
public class LectureSaisie {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel est votre prénom ?");
        // TODO : lire le prenom (toute la ligne)

        System.out.println("Quel est votre âge ?");
        // TODO : lire l'age (un entier)

        // TODO : afficher "Bonjour <prenom>, vous avez <age> ans."
    }
}
