package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.2.3 — cas limites.
 */
@DisplayName("ASCII art — tests privés")
class AsciiArtPriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> AsciiArt.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("hauteur 1 : une seule étoile")
    void hauteur_1() {
        assertThat(executer("1" + NL)).isEqualTo("*" + NL);
    }

    @Test
    @DisplayName("hauteur 5")
    void hauteur_5() {
        assertThat(executer("5" + NL))
            .isEqualTo("*" + NL + "**" + NL + "***" + NL + "****" + NL + "*****" + NL);
    }
}
