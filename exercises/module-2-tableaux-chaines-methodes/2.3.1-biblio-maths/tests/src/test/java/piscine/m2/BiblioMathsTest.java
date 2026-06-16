package piscine.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.3.1 Bibliotheque mathematique.
 * Les methodes sont testees par leur valeur de retour.
 */
@DisplayName("Biblio maths — tests publics")
class BiblioMathsTest {

    @Test
    @DisplayName("pgcd(12, 18) = 6")
    void pgcd_nominal() {
        assertThat(BiblioMaths.pgcd(12, 18))
            .as("le PGCD de 12 et 18 est 6")
            .isEqualTo(6);
    }

    @Test
    @DisplayName("estPremier(7) = true")
    void estPremier_vrai() {
        assertThat(BiblioMaths.estPremier(7))
            .as("7 est premier")
            .isTrue();
    }

    @Test
    @DisplayName("sommeChiffres(123) = 6")
    void sommeChiffres_nominal() {
        assertThat(BiblioMaths.sommeChiffres(123))
            .as("1 + 2 + 3 = 6")
            .isEqualTo(6);
    }
}
