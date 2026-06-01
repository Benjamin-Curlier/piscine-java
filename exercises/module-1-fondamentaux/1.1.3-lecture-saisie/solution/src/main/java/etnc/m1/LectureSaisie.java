package etnc.m1;

import java.util.Scanner;

/**
 * Exercice 1.1.3 — Lecture d'une saisie (solution de reference).
 *
 * <p>Lit deux informations au clavier puis les restitue dans un message.
 * On lit d'abord le prenom (toute la ligne) puis l'age (un entier) :
 * cet ordre evite le piege du tampon decrit au chapitre 1.4.</p>
 */
public class LectureSaisie {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quel est votre prénom ?");
        String prenom = clavier.nextLine();   // toute la ligne : gere les prenoms composes

        System.out.println("Quel est votre âge ?");
        int age = clavier.nextInt();

        System.out.println("Bonjour " + prenom + ", vous avez " + age + " ans.");
    }
}
