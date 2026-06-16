package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.2.3 Manipulation booleenne.
 *
 * Couvre les cas pieges de la regle : annees seculaires (1900 non, 2000 oui),
 * pour verifier que la condition sur 100 et 400 est bien prise en compte.
 */
@DisplayName("Année bissextile — tests privés")
class AnneeBissextilePriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> AnneeBissextile.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("1900 n'est pas bissextile (divisible par 100, pas par 400)")
    void annee_1900() {
        assertThat(executer("1900" + NL))
            .isEqualTo("Quelle année ?" + NL + "1900 n'est pas bissextile." + NL);
    }

    @Test
    @DisplayName("2000 est bissextile (divisible par 400)")
    void annee_2000() {
        assertThat(executer("2000" + NL))
            .isEqualTo("Quelle année ?" + NL + "2000 est bissextile." + NL);
    }

    @Test
    @DisplayName("2020 est bissextile")
    void annee_2020() {
        assertThat(executer("2020" + NL))
            .isEqualTo("Quelle année ?" + NL + "2020 est bissextile." + NL);
    }
}
