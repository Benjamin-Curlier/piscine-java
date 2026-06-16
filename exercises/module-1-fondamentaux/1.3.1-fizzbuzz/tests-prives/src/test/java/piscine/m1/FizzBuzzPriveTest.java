package piscine.m1;

import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.3.1 FizzBuzz.
 *
 * Reconstruit la sortie COMPLETE des 100 lignes et la compare a l'identique.
 */
@DisplayName("FizzBuzz — tests privés")
class FizzBuzzPriveTest {

    private static final String NL = System.lineSeparator();

    private static String ligne(int i) {
        if (i % 15 == 0) {
            return "FizzBuzz";
        } else if (i % 3 == 0) {
            return "Fizz";
        } else if (i % 5 == 0) {
            return "Buzz";
        }
        return Integer.toString(i);
    }

    @Test
    @DisplayName("la sortie des 100 lignes est exacte")
    void sortie_complete_exacte() {
        String sortie = CaptureSortie.capturer(() -> FizzBuzz.main(new String[]{}));

        StringBuilder attendu = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            attendu.append(ligne(i)).append(NL);
        }

        assertThat(sortie)
            .as("Les 100 lignes doivent correspondre exactement")
            .isEqualTo(attendu.toString());
    }
}
