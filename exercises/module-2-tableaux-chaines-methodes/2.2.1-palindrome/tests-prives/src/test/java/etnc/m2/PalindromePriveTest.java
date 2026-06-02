package etnc.m2;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.2.1 — cas limites.
 */
@DisplayName("Palindrome — tests privés")
class PalindromePriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> Palindrome.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("un seul caractère : oui")
    void un_caractere() {
        assertThat(executer("a" + NL)).isEqualTo("oui" + NL);
    }

    @Test
    @DisplayName("deux caractères identiques : oui")
    void deux_identiques() {
        assertThat(executer("aa" + NL)).isEqualTo("oui" + NL);
    }

    @Test
    @DisplayName("deux caractères différents : non")
    void deux_differents() {
        assertThat(executer("ab" + NL)).isEqualTo("non" + NL);
    }

    @Test
    @DisplayName("casse mixte : Ressasser -> oui")
    void casse_mixte() {
        assertThat(executer("Ressasser" + NL)).isEqualTo("oui" + NL);
    }

    @Test
    @DisplayName("longueur paire non palindrome : abca -> non")
    void paire_non_palindrome() {
        assertThat(executer("abca" + NL)).isEqualTo("non" + NL);
    }
}
