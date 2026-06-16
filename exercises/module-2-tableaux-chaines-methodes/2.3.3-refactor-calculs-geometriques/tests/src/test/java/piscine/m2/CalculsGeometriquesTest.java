package piscine.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 2.3.3 Calculs geometriques (refactor).
 * Les valeurs attendues du cercle sont calculees avec la meme formule
 * (Math.PI), ce qui permet une comparaison par egalite exacte.
 */
@DisplayName("Calculs géométriques — tests publics")
class CalculsGeometriquesTest {

    @Test
    @DisplayName("aire du cercle : aire(2.0) = pi * 2 * 2")
    void aire_cercle() {
        assertThat(CalculsGeometriques.aire(2.0))
            .as("aire d'un cercle de rayon 2")
            .isEqualTo(Math.PI * 2.0 * 2.0);
    }

    @Test
    @DisplayName("aire du rectangle : aire(3.0, 4.0) = 12.0")
    void aire_rectangle() {
        assertThat(CalculsGeometriques.aire(3.0, 4.0)).isEqualTo(12.0);
    }

    @Test
    @DisplayName("périmètre du cercle : perimetre(2.0) = 2 * pi * 2")
    void perimetre_cercle() {
        assertThat(CalculsGeometriques.perimetre(2.0))
            .isEqualTo(2.0 * Math.PI * 2.0);
    }

    @Test
    @DisplayName("périmètre du rectangle : perimetre(3.0, 4.0) = 14.0")
    void perimetre_rectangle() {
        assertThat(CalculsGeometriques.perimetre(3.0, 4.0)).isEqualTo(14.0);
    }
}
