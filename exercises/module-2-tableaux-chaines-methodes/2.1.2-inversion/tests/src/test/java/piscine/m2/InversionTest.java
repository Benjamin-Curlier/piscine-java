package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.1.2 Inversion.
 * La sortie tient sur une ligne ; on compare apres strip() pour tolerer
 * un eventuel espace final.
 */
@DisplayName("Inversion — tests publics")
class InversionTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> Inversion.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("cas nominal : 1 2 3 4 -> 4 3 2 1")
    void cas_nominal() {
        assertThat(executer("4" + NL + "1 2 3 4" + NL).strip())
            .as("les éléments doivent être affichés dans l'ordre inverse")
            .isEqualTo("4 3 2 1");
    }

    @Test
    @DisplayName("trois éléments")
    void trois_elements() {
        assertThat(executer("3" + NL + "5 6 7" + NL).strip())
            .isEqualTo("7 6 5");
    }
}
