package piscine.m1;

/**
 * Exercice 1.3.1 — FizzBuzz (solution de reference).
 *
 * <p>Parcourt 1 a 100 et applique les regles FizzBuzz. On teste le multiple
 * de 15 EN PREMIER : sinon, un nombre comme 15 serait deja capte par le test
 * "multiple de 3" et n'afficherait jamais "FizzBuzz".</p>
 */
public class FizzBuzz {

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}
