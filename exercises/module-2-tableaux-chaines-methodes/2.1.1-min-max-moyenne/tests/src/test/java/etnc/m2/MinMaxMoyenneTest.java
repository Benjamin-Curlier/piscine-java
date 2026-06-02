package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.1.1 Min, max et moyenne.
 */
@DisplayName("Min, max, moyenne — tests publics")
class MinMaxMoyenneTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> MinMaxMoyenne.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("cas nominal : 3 9 1 7 5")
    void cas_nominal() {
        assertThat(executer("5" + NL + "3 9 1 7 5" + NL))
            .as("min=1, max=9, moyenne=5.00")
            .isEqualTo("Min : 1" + NL + "Max : 9" + NL + "Moyenne : 5.00" + NL);
    }

    @Test
    @DisplayName("moyenne non entière formatée à 2 décimales avec un point")
    void moyenne_decimale() {
        assertThat(executer("2" + NL + "10 3" + NL))
            .as("moyenne de 10 et 3 = 6.50")
            .isEqualTo("Min : 3" + NL + "Max : 10" + NL + "Moyenne : 6.50" + NL);
    }
}
