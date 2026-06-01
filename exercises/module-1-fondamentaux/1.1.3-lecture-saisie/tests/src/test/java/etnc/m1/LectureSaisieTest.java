package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.1.3 Lecture d'une saisie.
 */
@DisplayName("Lecture d'une saisie — tests publics")
class LectureSaisieTest {

    private static final String NL = System.lineSeparator();

    @Test
    @DisplayName("lit un prénom et un âge, puis affiche le message attendu")
    void lit_prenom_et_age() {
        String[] sortie = new String[1];

        CaptureEntree.avecEntree("Dupont" + NL + "42" + NL,
            () -> sortie[0] = CaptureSortie.capturer(() -> LectureSaisie.main(new String[]{})));

        String attendu =
            "Quel est votre prénom ?" + NL
            + "Quel est votre âge ?" + NL
            + "Bonjour Dupont, vous avez 42 ans." + NL;

        assertThat(sortie[0])
            .as("La sortie doit reprendre le prénom et l'âge saisis")
            .isEqualTo(attendu);
    }
}
