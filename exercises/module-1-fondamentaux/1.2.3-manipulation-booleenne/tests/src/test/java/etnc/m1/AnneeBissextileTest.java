package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.2.3 Manipulation booleenne (annee bissextile).
 */
@DisplayName("Année bissextile — tests publics")
class AnneeBissextileTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> AnneeBissextile.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("2024 est bissextile")
    void annee_2024_bissextile() {
        assertThat(executer("2024" + NL))
            .isEqualTo("Quelle année ?" + NL + "2024 est bissextile." + NL);
    }

    @Test
    @DisplayName("2023 n'est pas bissextile")
    void annee_2023_non_bissextile() {
        assertThat(executer("2023" + NL))
            .isEqualTo("Quelle année ?" + NL + "2023 n'est pas bissextile." + NL);
    }
}
