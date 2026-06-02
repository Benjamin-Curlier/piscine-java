package etnc.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.3.2 — cas limites.
 */
@DisplayName("Surcharge — tests privés")
class SurchargePriveTest {

    @Test
    @DisplayName("maximum(int, int) avec négatifs : maximum(-3, -7) = -3")
    void deux_int_negatifs() {
        assertThat(Surcharge.maximum(-3, -7)).isEqualTo(-3);
    }

    @Test
    @DisplayName("maximum(int, int) valeurs égales : maximum(4, 4) = 4")
    void deux_int_egaux() {
        assertThat(Surcharge.maximum(4, 4)).isEqualTo(4);
    }

    @Test
    @DisplayName("maximum(int, int, int) max au milieu : maximum(1, 9, 5) = 9")
    void trois_int_milieu() {
        assertThat(Surcharge.maximum(1, 9, 5)).isEqualTo(9);
    }

    @Test
    @DisplayName("maximum(int, int, int) max en tête : maximum(9, 1, 5) = 9")
    void trois_int_tete() {
        assertThat(Surcharge.maximum(9, 1, 5)).isEqualTo(9);
    }

    @Test
    @DisplayName("maximum(double, double) avec négatifs : maximum(-1.5, -0.5) = -0.5")
    void deux_double_negatifs() {
        assertThat(Surcharge.maximum(-1.5, -0.5)).isEqualTo(-0.5);
    }
}
