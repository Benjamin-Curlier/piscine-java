package etnc.m1;

import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Tests publics de l'exercice 1.1.2 Affichage formate.
 */
@DisplayName("Affichage formaté — tests publics")
class AffichageFormateTest {

    private static final String NL = System.lineSeparator();

    @Test
    @DisplayName("affiche la fiche exactement au format attendu")
    void affiche_la_fiche_au_format_attendu() {
        String sortie = CaptureSortie.capturer(() -> AffichageFormate.main(new String[]{}));

        String attendu =
            "=== Fiche militaire ===" + NL
            + "Nom    : Martin" + NL
            + "Grade  : Sergent" + NL
            + "Age    : 29 ans" + NL;

        assertThat(sortie)
            .as("La fiche doit etre affichee exactement au format demande")
            .isEqualTo(attendu);
    }

    @Test
    @DisplayName("ne leve pas d'exception")
    void ne_leve_pas_d_exception() {
        assertThatNoException()
            .as("main(...) doit s'executer proprement")
            .isThrownBy(() -> AffichageFormate.main(new String[]{}));
    }
}
