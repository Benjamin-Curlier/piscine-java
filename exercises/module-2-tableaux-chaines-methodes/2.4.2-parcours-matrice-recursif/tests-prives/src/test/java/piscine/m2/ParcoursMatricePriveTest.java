package piscine.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.4.2 — cas limites.
 */
@DisplayName("Parcours récursif de matrice — tests privés")
class ParcoursMatricePriveTest {

    @Test
    @DisplayName("matrice 3x3 : somme = 45")
    void somme_3x3() {
        int[][] matrice = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(45);
    }

    @Test
    @DisplayName("valeurs négatives : [[-1,-2],[-3,-4]] = -10")
    void somme_negatifs() {
        int[][] matrice = {{-1, -2}, {-3, -4}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(-10);
    }

    @Test
    @DisplayName("matrice rectangulaire 2x3 : [[1,2,3],[4,5,6]] = 21")
    void somme_rectangulaire() {
        int[][] matrice = {{1, 2, 3}, {4, 5, 6}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(21);
    }

    @Test
    @DisplayName("une seule ligne : [[2,4,6,8]] = 20")
    void somme_une_ligne() {
        int[][] matrice = {{2, 4, 6, 8}};
        assertThat(ParcoursMatrice.sommeMatrice(matrice)).isEqualTo(20);
    }
}
