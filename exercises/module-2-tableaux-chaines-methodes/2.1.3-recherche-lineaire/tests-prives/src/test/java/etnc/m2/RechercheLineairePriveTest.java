package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.1.3 — cas limites.
 */
@DisplayName("Recherche linéaire — tests privés")
class RechercheLineairePriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> RechercheLineaire.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("première position : indice 0")
    void premiere_position() {
        assertThat(executer("3" + NL + "7 8 9" + NL + "7" + NL))
            .isEqualTo("Indice : 0" + NL);
    }

    @Test
    @DisplayName("dernière position")
    void derniere_position() {
        assertThat(executer("3" + NL + "7 8 9" + NL + "9" + NL))
            .isEqualTo("Indice : 2" + NL);
    }

    @Test
    @DisplayName("doublons : renvoie la PREMIÈRE occurrence")
    void doublons_premiere_occurrence() {
        assertThat(executer("5" + NL + "4 2 4 2 4" + NL + "2" + NL))
            .isEqualTo("Indice : 1" + NL);
    }

    @Test
    @DisplayName("tableau à un élément, valeur absente")
    void un_element_absent() {
        assertThat(executer("1" + NL + "5" + NL + "6" + NL))
            .isEqualTo("Absent" + NL);
    }
}
