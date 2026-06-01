package etnc.m1;

import etnc.util.CaptureEntree;
import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.3.3 Table de multiplication.
 *
 * Cas limites : zero (toutes les lignes a 0) et nombre negatif (produits negatifs).
 */
@DisplayName("Table de multiplication — tests privés")
class TableMultiplicationPriveTest {

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
    @DisplayName("table de 0")
    void table_de_0() {
        assertThat(executer("0" + NL)).isEqualTo(attendu(0));
    }

    @Test
    @DisplayName("table d'un nombre négatif")
    void table_negative() {
        assertThat(executer("-3" + NL)).isEqualTo(attendu(-3));
    }
}
