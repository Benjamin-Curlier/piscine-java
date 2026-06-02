package etnc.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.4.2 Parcours recursif de matrice.
 */
@DisplayName("Parcours récursif de matrice — tests publics")
class ParcoursMatriceTest {

    @Test
    @DisplayName("somme d'une matrice 2x2 : [[1,2],[3,4]] = 10")
    void somme_2x2() {
        int[][] matrice = {{1, 2}, {3, 4}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(10);
    }

    @Test
    @DisplayName("somme d'une matrice 1x1 : [[5]] = 5")
    void somme_1x1() {
        int[][] matrice = {{5}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(5);
    }
}
