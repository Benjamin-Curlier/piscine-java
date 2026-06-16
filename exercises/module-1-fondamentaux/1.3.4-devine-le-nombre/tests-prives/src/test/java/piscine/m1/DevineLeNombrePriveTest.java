package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.3.4 Devine le nombre.
 *
 * On recalcule le secret avec la graine 1789 et on SIMULE la sortie attendue
 * pour une sequence d'essais donnee : la comparaison reste valable quelle que
 * soit la valeur exacte du secret.
 */
@DisplayName("Devine le nombre — tests privés")
class DevineLeNombrePriveTest {

    private static final String NL = System.lineSeparator();
    private static final int SECRET = new Random(1789).nextInt(100) + 1;

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> DevineLeNombre.main(new String[]{})));
        return sortie[0];
    }

    /** Construit l'entree clavier (une ligne par essai). */
    private static String entree(int... essais) {
        StringBuilder sb = new StringBuilder();
        for (int e : essais) {
            sb.append(e).append(NL);
        }
        return sb.toString();
    }

    /** Reproduit la sortie attendue de la solution pour la sequence d'essais. */
    private static String attendu(int... essais) {
        StringBuilder sb = new StringBuilder("Devinez le nombre (entre 1 et 100) :").append(NL);
        int count = 0;
        for (int e : essais) {
            count++;
            if (e < SECRET) {
                sb.append("C'est plus grand.").append(NL);
            } else if (e > SECRET) {
                sb.append("C'est plus petit.").append(NL);
            } else {
                sb.append("Bravo, vous avez trouvé en ").append(count).append(" essai(s) !").append(NL);
                break;
            }
        }
        return sb.toString();
    }

    @Test
    @DisplayName("un essai trop bas puis le bon")
    void essai_trop_bas_puis_bon() {
        int bas = SECRET > 1 ? 1 : SECRET;   // 1 est <= secret ; si secret==1, on gagne direct
        assertThat(executer(entree(bas, SECRET)))
            .isEqualTo(attendu(bas, SECRET));
    }

    @Test
    @DisplayName("un essai trop haut puis le bon")
    void essai_trop_haut_puis_bon() {
        int haut = SECRET < 100 ? 100 : SECRET;   // 100 est >= secret ; si secret==100, on gagne direct
        assertThat(executer(entree(haut, SECRET)))
            .isEqualTo(attendu(haut, SECRET));
    }

    @Test
    @DisplayName("plusieurs essais comptés correctement")
    void plusieurs_essais() {
        assertThat(executer(entree(1, 100, SECRET)))
            .isEqualTo(attendu(1, 100, SECRET));
    }
}
