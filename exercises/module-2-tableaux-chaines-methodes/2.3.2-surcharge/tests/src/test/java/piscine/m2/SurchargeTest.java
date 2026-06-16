package piscine.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.3.2 Surcharge.
 */
@DisplayName("Surcharge — tests publics")
class SurchargeTest {

    @Test
    @DisplayName("maximum(int, int) : maximum(3, 7) = 7")
    void maximum_deux_int() {
        assertThat(Surcharge.maximum(3, 7))
            .as("le plus grand de 3 et 7 est 7")
            .isEqualTo(7);
    }

    @Test
    @DisplayName("maximum(int, int, int) : maximum(3, 7, 5) = 7")
    void maximum_trois_int() {
        assertThat(Surcharge.maximum(3, 7, 5)).isEqualTo(7);
    }

    @Test
    @DisplayName("maximum(double, double) : maximum(2.5, 1.5) = 2.5")
    void maximum_deux_double() {
        assertThat(Surcharge.maximum(2.5, 1.5)).isEqualTo(2.5);
    }
}
