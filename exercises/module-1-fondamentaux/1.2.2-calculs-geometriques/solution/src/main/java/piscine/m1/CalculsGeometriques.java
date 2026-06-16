package piscine.m1;

import java.util.Scanner;

/**
 * Exercice 1.2.2 — Calculs geometriques (solution de reference).
 *
 * <p>Calcule l'aire et le perimetre d'un cercle a partir de son rayon.
 * On reutilise la constante {@code Math.PI} fournie par la bibliotheque
 * standard plutot que d'ecrire une valeur approchee de pi.</p>
 */
public class CalculsGeometriques {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Rayon du cercle ?");
        double rayon = clavier.nextDouble();

        double aire = Math.PI * rayon * rayon;
        double perimetre = 2 * Math.PI * rayon;

        System.out.println("Aire : " + aire);
        System.out.println("Périmètre : " + perimetre);
    }
}
