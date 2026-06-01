package etnc.m1;

import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.3.1 FizzBuzz.
 *
 * Verifie le debut de la sortie et la presence des valeurs cles.
 */
@DisplayName("FizzBuzz — tests publics")
class FizzBuzzTest {

    private static final String NL = System.lineSeparator();

    /** Reconstruit la ligne attendue pour un nombre donne. */
    static String ligne(int i) {
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
    @DisplayName("les 15 premières lignes sont correctes")
    void debut_correct() {
        String sortie = CaptureSortie.capturer(() -> FizzBuzz.main(new String[]{}));

        StringBuilder debut = new StringBuilder();
        for (int i = 1; i <= 15; i++) {
            debut.append(ligne(i)).append(NL);
        }

        assertThat(sortie)
            .as("Le début doit suivre la règle FizzBuzz (15 → FizzBuzz)")
            .startsWith(debut.toString());
    }

    @Test
    @DisplayName("contient les cas Fizz, Buzz et FizzBuzz")
    void contient_les_cas_cles() {
        String sortie = CaptureSortie.capturer(() -> FizzBuzz.main(new String[]{}));

        assertThat(sortie)
            .contains(NL + "Fizz" + NL)        // 3
            .contains(NL + "Buzz" + NL)        // 5
            .contains(NL + "FizzBuzz" + NL);   // 15
    }
}
