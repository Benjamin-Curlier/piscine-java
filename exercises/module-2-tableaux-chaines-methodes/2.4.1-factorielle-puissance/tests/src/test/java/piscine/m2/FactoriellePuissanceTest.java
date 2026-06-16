package piscine.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.4.1 Factorielle et puissance.
 */
@DisplayName("Factorielle et puissance — tests publics")
class FactoriellePuissanceTest {

    @Test
    @DisplayName("factorielle(0) = 1 (cas de base)")
    void factorielle_zero() {
        assertThat(FactoriellePuissance.factorielle(0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("factorielle(5) = 120")
    void factorielle_cinq() {
        assertThat(FactoriellePuissance.factorielle(5)).isEqualTo(120L);
    }

    @Test
    @DisplayName("puissance(2, 10) = 1024")
    void puissance_nominale() {
        assertThat(FactoriellePuissance.puissance(2, 10)).isEqualTo(1024L);
    }

    @Test
    @DisplayName("puissance(5, 0) = 1 (cas de base)")
    void puissance_exposant_zero() {
        assertThat(FactoriellePuissance.puissance(5, 0)).isEqualTo(1L);
    }
}
