package etnc.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.3.1 — cas limites.
 */
@DisplayName("Biblio maths — tests privés")
class BiblioMathsPriveTest {

    @Test
    @DisplayName("pgcd de nombres premiers entre eux : 17 et 13 -> 1")
    void pgcd_coprimes() {
        assertThat(BiblioMaths.pgcd(17, 13)).isEqualTo(1);
    }

    @Test
    @DisplayName("pgcd quand l'un divise l'autre : 100 et 10 -> 10")
    void pgcd_multiple() {
        assertThat(BiblioMaths.pgcd(100, 10)).isEqualTo(10);
    }

    @Test
    @DisplayName("pgcd avec zéro : pgcd(0, 5) -> 5")
    void pgcd_avec_zero() {
        assertThat(BiblioMaths.pgcd(0, 5)).isEqualTo(5);
    }

    @Test
    @DisplayName("estPremier(1) = false")
    void estPremier_un() {
        assertThat(BiblioMaths.estPremier(1)).isFalse();
    }

    @Test
    @DisplayName("estPremier(2) = true")
    void estPremier_deux() {
        assertThat(BiblioMaths.estPremier(2)).isTrue();
    }

    @Test
    @DisplayName("estPremier(97) = true")
    void estPremier_grand_premier() {
        assertThat(BiblioMaths.estPremier(97)).isTrue();
    }

    @Test
    @DisplayName("estPremier(100) = false")
    void estPremier_compose() {
        assertThat(BiblioMaths.estPremier(100)).isFalse();
    }

    @Test
    @DisplayName("sommeChiffres(0) = 0")
    void sommeChiffres_zero() {
        assertThat(BiblioMaths.sommeChiffres(0)).isEqualTo(0);
    }

    @Test
    @DisplayName("sommeChiffres(9999) = 36")
    void sommeChiffres_grand() {
        assertThat(BiblioMaths.sommeChiffres(9999)).isEqualTo(36);
    }
}
