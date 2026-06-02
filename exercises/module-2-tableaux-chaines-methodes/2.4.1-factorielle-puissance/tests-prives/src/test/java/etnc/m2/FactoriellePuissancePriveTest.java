package etnc.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.4.1 — cas limites.
 */
@DisplayName("Factorielle et puissance — tests privés")
class FactoriellePuissancePriveTest {

    @Test
    @DisplayName("factorielle(1) = 1")
    void factorielle_un() {
        assertThat(FactoriellePuissance.factorielle(1)).isEqualTo(1L);
    }

    @Test
    @DisplayName("factorielle(10) = 3 628 800")
    void factorielle_dix() {
        assertThat(FactoriellePuissance.factorielle(10)).isEqualTo(3628800L);
    }

    @Test
    @DisplayName("factorielle(13) dépasse la capacité d'un int (besoin de long)")
    void factorielle_treize() {
        assertThat(FactoriellePuissance.factorielle(13)).isEqualTo(6227020800L);
    }

    @Test
    @DisplayName("puissance(2, 0) = 1")
    void puissance_base_deux_exposant_zero() {
        assertThat(FactoriellePuissance.puissance(2, 0)).isEqualTo(1L);
    }

    @Test
    @DisplayName("puissance(3, 4) = 81")
    void puissance_trois_quatre() {
        assertThat(FactoriellePuissance.puissance(3, 4)).isEqualTo(81L);
    }

    @Test
    @DisplayName("puissance(7, 1) = 7")
    void puissance_exposant_un() {
        assertThat(FactoriellePuissance.puissance(7, 1)).isEqualTo(7L);
    }

    @Test
    @DisplayName("puissance(1, 100) = 1")
    void puissance_base_un() {
        assertThat(FactoriellePuissance.puissance(1, 100)).isEqualTo(1L);
    }

    @Test
    @DisplayName("puissance(10, 9) = 1 000 000 000")
    void puissance_grande() {
        assertThat(FactoriellePuissance.puissance(10, 9)).isEqualTo(1000000000L);
    }
}
