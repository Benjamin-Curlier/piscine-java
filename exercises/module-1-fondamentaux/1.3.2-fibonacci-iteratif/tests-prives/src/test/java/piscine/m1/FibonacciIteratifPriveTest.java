package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.3.2 Fibonacci iteratif.
 *
 * Cas de base (0, 1, 2) et rang plus eleve (20) pour verifier le calcul long.
 */
@DisplayName("Fibonacci itératif — tests privés")
class FibonacciIteratifPriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> FibonacciIteratif.main(new String[]{})));
        return sortie[0];
    }

    private static String attendu(int n, long valeur) {
        return "Quel rang de la suite de Fibonacci ?" + NL + "F(" + n + ") = " + valeur + NL;
    }

    @Test
    @DisplayName("F(0) = 0")
    void f_0() {
        assertThat(executer("0" + NL)).isEqualTo(attendu(0, 0));
    }

    @Test
    @DisplayName("F(1) = 1")
    void f_1() {
        assertThat(executer("1" + NL)).isEqualTo(attendu(1, 1));
    }

    @Test
    @DisplayName("F(2) = 1")
    void f_2() {
        assertThat(executer("2" + NL)).isEqualTo(attendu(2, 1));
    }

    @Test
    @DisplayName("F(20) = 6765")
    void f_20() {
        assertThat(executer("20" + NL)).isEqualTo(attendu(20, 6765));
    }
}
