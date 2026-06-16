package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.1.2 — cas limites.
 */
@DisplayName("Inversion — tests privés")
class InversionPriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> Inversion.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("un seul élément : reste identique")
    void un_seul_element() {
        assertThat(executer("1" + NL + "7" + NL).strip()).isEqualTo("7");
    }

    @Test
    @DisplayName("deux éléments")
    void deux_elements() {
        assertThat(executer("2" + NL + "8 9" + NL).strip()).isEqualTo("9 8");
    }

    @Test
    @DisplayName("valeurs négatives et doublons")
    void negatifs_et_doublons() {
        assertThat(executer("5" + NL + "-1 4 4 -1 0" + NL).strip())
            .isEqualTo("0 -1 4 4 -1");
    }
}
