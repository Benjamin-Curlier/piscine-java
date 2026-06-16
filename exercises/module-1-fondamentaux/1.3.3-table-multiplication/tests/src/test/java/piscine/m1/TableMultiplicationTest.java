package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.3.3 Table de multiplication.
 *
 * L'attendu est reconstruit par la meme boucle que la solution.
 */
@DisplayName("Table de multiplication — tests publics")
class TableMultiplicationTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> TableMultiplication.main(new String[]{})));
        return sortie[0];
    }

    private static String attendu(int n) {
        StringBuilder sb = new StringBuilder("Quelle table ?").append(NL);
        for (int i = 1; i <= 10; i++) {
            sb.append(n).append(" x ").append(i).append(" = ").append(n * i).append(NL);
        }
        return sb.toString();
    }

    @Test
    @DisplayName("affiche la table de 7")
    void table_de_7() {
        assertThat(executer("7" + NL)).isEqualTo(attendu(7));
    }
}
