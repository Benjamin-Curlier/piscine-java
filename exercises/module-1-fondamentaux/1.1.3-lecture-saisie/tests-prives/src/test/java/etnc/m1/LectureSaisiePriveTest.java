package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.1.3 Lecture d'une saisie.
 *
 * Verifie que le prenom est bien lu en entier (toute la ligne), y compris
 * lorsqu'il contient des espaces : c'est l'interet de nextLine() par rapport
 * a next() qui s'arreterait au premier espace.
 */
@DisplayName("Lecture d'une saisie — tests prives")
class LectureSaisiePriveTest {

    private static final String NL = System.lineSeparator();

    @Test
    @DisplayName("lit correctement un prénom composé (avec espaces)")
    void lit_prenom_compose() {
        String[] sortie = new String[1];

        CaptureEntree.avecEntree("Jean Le Goff" + NL + "30" + NL,
            () -> sortie[0] = CaptureSortie.capturer(() -> LectureSaisie.main(new String[]{})));

        assertThat(sortie[0])
            .as("Le prénom complet, espaces compris, doit être repris")
            .contains("Bonjour Jean Le Goff, vous avez 30 ans.");
    }

    @Test
    @DisplayName("fonctionne avec d'autres valeurs")
    void fonctionne_avec_d_autres_valeurs() {
        String[] sortie = new String[1];

        CaptureEntree.avecEntree("Sara" + NL + "25" + NL,
            () -> sortie[0] = CaptureSortie.capturer(() -> LectureSaisie.main(new String[]{})));

        assertThat(sortie[0])
            .contains("Bonjour Sara, vous avez 25 ans.");
    }
}
