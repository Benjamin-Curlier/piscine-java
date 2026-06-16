package piscine.m6;

/** Implémentation correcte de référence (identique au starter). */
public class Calculatrice {

    public int ajouter(int a, int b) {
        return a + b;
    }

    public int soustraire(int a, int b) {
        return a - b;
    }

    public int multiplier(int a, int b) {
        return a * b;
    }

    public int diviser(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("division par zéro");
        }
        return a / b;
    }
}
