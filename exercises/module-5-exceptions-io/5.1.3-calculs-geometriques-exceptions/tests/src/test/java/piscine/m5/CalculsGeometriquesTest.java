package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 5.1.3 — Calculs géométriques avec gardes.
 */
@DisplayName("Calculs géométriques avec exceptions — tests publics")
class CalculsGeometriquesTest {

    @Test
    @DisplayName("aire du cercle : aire(2.0) ≈ π × 4")
    void aire_cercle_rayon2() {
        assertThat(CalculsGeometriques.aire(2.0))
            .as("aire d'un cercle de rayon 2")
            .isCloseTo(Math.PI * 4, within(1e-9));
    }

    @Test
    @DisplayName("aire du rectangle : aire(3.0, 4.0) = 12.0")
    void aire_rectangle_3x4() {
        assertThat(CalculsGeometriques.aire(3.0, 4.0)).isEqualTo(12.0);
    }

    @Test
    @DisplayName("périmètre du cercle : perimetre(2.0) ≈ 4 × π")
    void perimetre_cercle_rayon2() {
        assertThat(CalculsGeometriques.perimetre(2.0))
            .as("périmètre d'un cercle de rayon 2")
            .isCloseTo(4 * Math.PI, within(1e-9));
    }

    @Test
    @DisplayName("périmètre du rectangle : perimetre(3.0, 4.0) = 14.0")
    void perimetre_rectangle_3x4() {
        assertThat(CalculsGeometriques.perimetre(3.0, 4.0)).isEqualTo(14.0);
    }

    @Test
    @DisplayName("aire cercle rayon négatif → IllegalArgumentException")
    void aire_cercle_rayon_negatif() {
        assertThatThrownBy(() -> CalculsGeometriques.aire(-1.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("périmètre cercle rayon négatif → IllegalArgumentException")
    void perimetre_cercle_rayon_negatif() {
        assertThatThrownBy(() -> CalculsGeometriques.perimetre(-1.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("aire rectangle largeur négative → IllegalArgumentException")
    void aire_rectangle_largeur_negative() {
        assertThatThrownBy(() -> CalculsGeometriques.aire(-1.0, 4.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("aire rectangle hauteur négative → IllegalArgumentException")
    void aire_rectangle_hauteur_negative() {
        assertThatThrownBy(() -> CalculsGeometriques.aire(3.0, -4.0))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
