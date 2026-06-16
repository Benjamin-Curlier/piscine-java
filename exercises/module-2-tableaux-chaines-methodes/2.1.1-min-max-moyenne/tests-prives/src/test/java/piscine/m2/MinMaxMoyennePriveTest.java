package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.1.1 — cas limites.
 */
@DisplayName("Min, max, moyenne — tests privés")
class MinMaxMoyennePriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> MinMaxMoyenne.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("tableau à un seul élément")
    void un_seul_element() {
        assertThat(executer("1" + NL + "42" + NL))
            .isEqualTo("Min : 42" + NL + "Max : 42" + NL + "Moyenne : 42.00" + NL);
    }

    @Test
    @DisplayName("valeurs négatives")
    void valeurs_negatives() {
        assertThat(executer("3" + NL + "-5 -1 -9" + NL))
            .isEqualTo("Min : -9" + NL + "Max : -1" + NL + "Moyenne : -5.00" + NL);
    }

    @Test
    @DisplayName("moyenne arrondie au centième (1 2 2 -> 1.67)")
    void moyenne_arrondie() {
        assertThat(executer("3" + NL + "1 2 2" + NL))
            .isEqualTo("Min : 1" + NL + "Max : 2" + NL + "Moyenne : 1.67" + NL);
    }
}
