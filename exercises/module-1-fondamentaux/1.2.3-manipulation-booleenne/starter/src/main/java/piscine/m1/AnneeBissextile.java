package piscine.m1;

import java.util.Scanner;

/**
 * Exercice 1.2.3 — Manipulation booleenne (annee bissextile).
 *
 * Completez la methode main pour lire une annee et dire si elle est bissextile.
 *
 * Regle : une annee est bissextile si elle est divisible par 4,
 * SAUF les annees divisibles par 100 mais pas par 400.
 * Autrement dit : (annee % 4 == 0 && annee % 100 != 0) || annee % 400 == 0
 *
 * Ne modifiez ni le nom de la classe, ni le package.
 */
public class AnneeBissextile {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Quelle année ?");
        // TODO : lire l'annee (un entier)
        // TODO : calculer un boolean indiquant si elle est bissextile
        // TODO : afficher "<annee> est bissextile." ou "<annee> n'est pas bissextile."
    }
}
