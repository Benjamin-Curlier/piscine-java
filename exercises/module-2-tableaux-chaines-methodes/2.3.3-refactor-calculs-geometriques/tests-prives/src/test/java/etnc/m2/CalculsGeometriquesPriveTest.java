package etnc.m2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 2.3.3 — cas limites.
 */
@DisplayName("Calculs géométriques — tests privés")
class CalculsGeometriquesPriveTest {

    @Test
    @DisplayName("aire du cercle de rayon 1 = pi")
    void aire_cercle_unite() {
        assertThat(CalculsGeometriques.aire(1.0)).isEqualTo(Math.PI);
    }

    @Test
    @DisplayName("périmètre du cercle de rayon 1 = 2 * pi")
    void perimetre_cercle_unite() {
        assertThat(CalculsGeometriques.perimetre(1.0)).isEqualTo(2.0 * Math.PI);
    }

    @Test
    @DisplayName("aire du rectangle 5 x 2 = 10.0")
    void aire_rectangle_5x2() {
        assertThat(CalculsGeometriques.aire(5.0, 2.0)).isEqualTo(10.0);
    }

    @Test
    @DisplayName("périmètre du rectangle 5 x 2 = 14.0")
    void perimetre_rectangle_5x2() {
        assertThat(CalculsGeometriques.perimetre(5.0, 2.0)).isEqualTo(14.0);
    }

    @Test
    @DisplayName("carré vu comme rectangle : aire(3.0, 3.0) = 9.0")
    void aire_carre_via_rectangle() {
        assertThat(CalculsGeometriques.aire(3.0, 3.0)).isEqualTo(9.0);
    }
}
