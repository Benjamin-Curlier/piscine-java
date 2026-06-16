package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.2.1 Conversion d'unites.
 *
 * L'attendu est construit avec la meme formule que la solution, ce qui evite
 * de deviner la representation exacte des nombres a virgule.
 */
@DisplayName("Conversion d'unités — tests publics")
class ConversionUnitesTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> ConversionUnites.main(new String[]{})));
        return sortie[0];
    }

    private static String attendu(double celsius) {
        double fahrenheit = celsius * 9 / 5 + 32;
        return "Température en °C ?" + NL
            + celsius + " °C = " + fahrenheit + " °F" + NL;
    }

    @Test
    @DisplayName("convertit 100 °C en 212 °F")
    void convertit_100_degres() {
        assertThat(executer("100" + NL))
            .as("100 °C doit donner 212 °F")
            .isEqualTo(attendu(100.0));
    }
}
