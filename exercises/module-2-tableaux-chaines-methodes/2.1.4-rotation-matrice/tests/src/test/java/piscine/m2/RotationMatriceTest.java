package piscine.m2;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.1.4 Rotation de matrice.
 * On normalise la sortie (strip par ligne) pour tolerer un espace final.
 */
@DisplayName("Rotation de matrice — tests publics")
class RotationMatriceTest {

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> RotationMatrice.main(new String[]{})));
        return sortie[0];
    }

    /** Recompose la sortie en lignes nettoyees (tolere espace final et type de saut de ligne). */
    private static String normaliser(String s) {
        StringBuilder sb = new StringBuilder();
        for (String ligne : s.strip().split("\\R")) {
            sb.append(ligne.strip()).append('\n');
        }
        return sb.toString();
    }

    @Test
    @DisplayName("matrice 2x2 tournée 90° horaire")
    void rotation_2x2() {
        String attendu = "3 1\n4 2\n";
        assertThat(normaliser(executer("2\n1 2\n3 4\n")))
            .as("[[1,2],[3,4]] tourné horaire -> [[3,1],[4,2]]")
            .isEqualTo(attendu);
    }

    @Test
    @DisplayName("matrice 3x3 tournée 90° horaire")
    void rotation_3x3() {
        String attendu = "7 4 1\n8 5 2\n9 6 3\n";
        assertThat(normaliser(executer("3\n1 2 3\n4 5 6\n7 8 9\n")))
            .isEqualTo(attendu);
    }
}
