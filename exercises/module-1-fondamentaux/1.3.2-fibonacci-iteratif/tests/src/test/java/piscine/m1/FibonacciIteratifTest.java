package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.3.2 Fibonacci iteratif.
 */
@DisplayName("Fibonacci itératif — tests publics")
class FibonacciIteratifTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> FibonacciIteratif.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("F(10) = 55")
    void f_10() {
        assertThat(executer("10" + NL))
            .isEqualTo("Quel rang de la suite de Fibonacci ?" + NL + "F(10) = 55" + NL);
    }
}
