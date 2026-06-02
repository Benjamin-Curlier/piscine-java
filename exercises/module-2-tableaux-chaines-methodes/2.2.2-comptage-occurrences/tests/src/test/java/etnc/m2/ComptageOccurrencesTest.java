package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.2.2 Comptage d'occurrences.
 */
@DisplayName("Comptage d'occurrences — tests publics")
class ComptageOccurrencesTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> ComptageOccurrences.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("compte le caractère 's' dans mississippi")
    void compte_s() {
        assertThat(executer("mississippi" + NL + "s" + NL))
            .as("mississippi contient 4 's'")
            .isEqualTo("Occurrences : 4" + NL);
    }

    @Test
    @DisplayName("compte le caractère 'i' dans mississippi")
    void compte_i() {
        assertThat(executer("mississippi" + NL + "i" + NL))
            .isEqualTo("Occurrences : 4" + NL);
    }
}
