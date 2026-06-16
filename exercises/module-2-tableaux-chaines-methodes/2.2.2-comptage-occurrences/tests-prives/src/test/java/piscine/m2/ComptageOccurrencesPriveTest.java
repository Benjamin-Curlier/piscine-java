package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.2.2 — cas limites.
 */
@DisplayName("Comptage d'occurrences — tests privés")
class ComptageOccurrencesPriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> ComptageOccurrences.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("caractère absent : 0")
    void caractere_absent() {
        assertThat(executer("mississippi" + NL + "z" + NL))
            .isEqualTo("Occurrences : 0" + NL);
    }

    @Test
    @DisplayName("casse stricte : 'M' majuscule compté une fois")
    void casse_majuscule() {
        assertThat(executer("Mississippi" + NL + "M" + NL))
            .isEqualTo("Occurrences : 1" + NL);
    }

    @Test
    @DisplayName("casse stricte : 'm' minuscule absent dans Mississippi")
    void casse_minuscule() {
        assertThat(executer("Mississippi" + NL + "m" + NL))
            .isEqualTo("Occurrences : 0" + NL);
    }

    @Test
    @DisplayName("texte avec espaces : compte la lettre 'a'")
    void texte_avec_espaces() {
        assertThat(executer("a b a" + NL + "a" + NL))
            .isEqualTo("Occurrences : 2" + NL);
    }

    @Test
    @DisplayName("compte le caractère espace")
    void compte_espace() {
        assertThat(executer("a b a" + NL + " " + NL))
            .isEqualTo("Occurrences : 2" + NL);
    }
}
