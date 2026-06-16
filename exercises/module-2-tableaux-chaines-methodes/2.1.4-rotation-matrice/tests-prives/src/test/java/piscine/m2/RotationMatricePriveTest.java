package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.1.4 — cas limites.
 */
@DisplayName("Rotation de matrice — tests privés")
class RotationMatricePriveTest {

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> RotationMatrice.main(new String[]{})));
        return sortie[0];
    }

    private static String normaliser(String s) {
        StringBuilder sb = new StringBuilder();
        for (String ligne : s.strip().split("\\R")) {
            sb.append(ligne.strip()).append('\n');
        }
        return sb.toString();
    }

    @Test
    @DisplayName("matrice 1x1 : inchangée")
    void matrice_1x1() {
        assertThat(normaliser(executer("1\n5\n"))).isEqualTo("5\n");
    }

    @Test
    @DisplayName("valeurs négatives")
    void valeurs_negatives() {
        // [[-1, 2], [3, -4]] -> horaire -> [[3, -1], [-4, 2]]
        assertThat(normaliser(executer("2\n-1 2\n3 -4\n")))
            .isEqualTo("3 -1\n-4 2\n");
    }
}
