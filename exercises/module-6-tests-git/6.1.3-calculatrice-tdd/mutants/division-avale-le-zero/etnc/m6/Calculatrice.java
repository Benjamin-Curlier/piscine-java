package etnc.m6;

// MUTANT « division-avale-le-zero » : au lieu de lever, renvoie 0 quand b == 0.
// Tué par un test attendant qu'une division par zéro LÈVE ArithmeticException
// (le mutant ne lève pas, il renvoie 0).
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
            return 0;
        }
        return a / b;
    }
}
