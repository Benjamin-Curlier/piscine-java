package etnc.m6;

// MUTANT « multiplication-en-addition » : multiplier renvoie a + b.
// Tué par un test affirmant multiplier(6, 7) == 42 (le mutant donne 13).
public class Calculatrice {

    public int ajouter(int a, int b) {
        return a + b;
    }

    public int soustraire(int a, int b) {
        return a - b;
    }

    public int multiplier(int a, int b) {
        return a + b;
    }

    public int diviser(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("division par zéro");
        }
        return a / b;
    }
}
