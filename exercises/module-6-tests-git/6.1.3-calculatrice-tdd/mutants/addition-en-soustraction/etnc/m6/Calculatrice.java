package etnc.m6;

// MUTANT « addition-en-soustraction » : ajouter renvoie a - b.
// Tué par un test affirmant ajouter(2, 3) == 5 (le mutant donne -1).
public class Calculatrice {

    public int ajouter(int a, int b) {
        return a - b;
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
