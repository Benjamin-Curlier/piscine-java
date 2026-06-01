package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.3.4 Devine le nombre.
 *
 * On recalcule le secret avec la MEME graine que le starter (1789), ce qui rend
 * le jeu deterministe : on peut alors fournir l'essai gagnant et verifier la sortie.
 */
@DisplayName("Devine le nombre — tests publics")
class DevineLeNombreTest {

    private static final String NL = System.lineSeparator();
    private static final int SECRET = new Random(1789).nextInt(100) + 1;

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> DevineLeNombre.main(new String[]{})));
        return sortie[0];
    }

    @Test
    @DisplayName("trouvé du premier coup")
    void trouve_du_premier_coup() {
        assertThat(executer(SECRET + NL))
            .isEqualTo("Devinez le nombre (entre 1 et 100) :" + NL
                + "Bravo, vous avez trouvé en 1 essai(s) !" + NL);
    }
}
