package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.2.1 Palindrome.
 */
@DisplayName("Palindrome — tests publics")
class PalindromeTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> Palindrome.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("mot palindrome (casse ignorée) : Radar -> oui")
    void mot_palindrome() {
        assertThat(executer("Radar" + NL))
            .as("Radar se lit pareil dans les deux sens")
            .isEqualTo("oui" + NL);
    }

    @Test
    @DisplayName("mot non palindrome : bonjour -> non")
    void mot_non_palindrome() {
        assertThat(executer("bonjour" + NL))
            .isEqualTo("non" + NL);
    }
}
