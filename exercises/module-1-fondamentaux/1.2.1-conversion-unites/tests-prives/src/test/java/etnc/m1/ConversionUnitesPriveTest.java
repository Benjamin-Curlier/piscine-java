package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.2.1 Conversion d'unites.
 *
 * Cas limites : zero, point fixe (-40), valeur usuelle.
 */
@DisplayName("Conversion d'unités — tests privés")
class ConversionUnitesPriveTest {

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
    @DisplayName("0 °C donne 32 °F")
    void zero_degre() {
        assertThat(executer("0" + NL)).isEqualTo(attendu(0.0));
    }

    @Test
    @DisplayName("-40 °C est le point d'égalité (-40 °F)")
    void point_fixe() {
        assertThat(executer("-40" + NL)).isEqualTo(attendu(-40.0));
    }

    @Test
    @DisplayName("37 °C donne 98.6 °F")
    void temperature_corporelle() {
        assertThat(executer("37" + NL)).isEqualTo(attendu(37.0));
    }
}
