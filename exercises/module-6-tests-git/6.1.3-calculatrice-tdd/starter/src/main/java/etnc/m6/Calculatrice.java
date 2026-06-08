package etnc.m6;

/**
 * Classe FOURNIE et CORRECTE. Ne la modifiez pas : écrivez la suite de tests qui la
 * valide (voir {@code src/test/java/etnc/m6/CalculatriceTest.java}).
 */
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

    /** Divise a par b. Lève {@link ArithmeticException} si b vaut 0. */
    public int diviser(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("division par zéro");
        }
        return a / b;
    }
}
