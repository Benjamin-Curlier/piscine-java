package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.1.3 Recherche lineaire.
 */
@DisplayName("Recherche linéaire — tests publics")
class RechercheLineaireTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> RechercheLineaire.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("valeur présente : renvoie son indice (base 0)")
    void valeur_presente() {
        assertThat(executer("3" + NL + "10 20 30" + NL + "20" + NL))
            .as("20 est en indice 1")
            .isEqualTo("Indice : 1" + NL);
    }

    @Test
    @DisplayName("valeur absente : affiche Absent")
    void valeur_absente() {
        assertThat(executer("3" + NL + "10 20 30" + NL + "99" + NL))
            .isEqualTo("Absent" + NL);
    }
}
