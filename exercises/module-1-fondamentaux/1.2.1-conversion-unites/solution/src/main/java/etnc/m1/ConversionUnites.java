package etnc.m1;

import java.util.Scanner;

/**
 * Exercice 1.2.1 — Conversion d'unites (solution de reference).
 *
 * <p>Lit une temperature en Celsius et la convertit en Fahrenheit. Le calcul
 * se fait en {@code double} : comme {@code celsius} est un {@code double},
 * toute l'expression est evaluee a virgule (pas de division entiere).</p>
 */
public class ConversionUnites {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("Température en °C ?");
        double celsius = clavier.nextDouble();

        double fahrenheit = celsius * 9 / 5 + 32;

        System.out.println(celsius + " °C = " + fahrenheit + " °F");
    }
}
