package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.2.3 ASCII art (triangle d'etoiles).
 */
@DisplayName("ASCII art — tests publics")
class AsciiArtTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> AsciiArt.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("triangle de hauteur 3")
    void hauteur_3() {
        assertThat(executer("3" + NL))
            .as("3 lignes : *, **, ***")
            .isEqualTo("*" + NL + "**" + NL + "***" + NL);
    }

    @Test
    @DisplayName("triangle de hauteur 2")
    void hauteur_2() {
        assertThat(executer("2" + NL))
            .isEqualTo("*" + NL + "**" + NL);
    }
}
