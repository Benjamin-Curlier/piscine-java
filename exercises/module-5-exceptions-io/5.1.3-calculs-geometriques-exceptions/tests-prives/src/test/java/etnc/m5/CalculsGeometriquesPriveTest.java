package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 5.1.3 — cas limites et vérification des messages.
 */
@DisplayName("Calculs géométriques avec exceptions — tests privés")
class CalculsGeometriquesPriveTest {

    @Test
    @DisplayName("rayon = 0 : aire et périmètre valent 0.0 (zéro autorisé)")
    void rayon_zero_autorise() {
        assertThat(CalculsGeometriques.aire(0.0)).isEqualTo(0.0);
        assertThat(CalculsGeometriques.perimetre(0.0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("rectangle un côté nul : aire(0.0, 5.0) = 0.0")
    void rectangle_cote_nul_autorise() {
        assertThat(CalculsGeometriques.aire(0.0, 5.0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("carré vu comme rectangle : aire(5.0, 5.0) = 25.0")
    void carre_via_rectangle() {
        assertThat(CalculsGeometriques.aire(5.0, 5.0)).isEqualTo(25.0);
    }

    @Test
    @DisplayName("périmètre rectangle largeur négative → IllegalArgumentException")
    void perimetre_rectangle_largeur_negative() {
        assertThatThrownBy(() -> CalculsGeometriques.perimetre(-1.0, 4.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("périmètre rectangle hauteur négative → IllegalArgumentException")
    void perimetre_rectangle_hauteur_negative() {
        assertThatThrownBy(() -> CalculsGeometriques.perimetre(3.0, -4.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("message nomme le paramètre fautif : aire(-1.0) contient 'rayon'")
    void message_contient_nom_parametre_rayon() {
        assertThatThrownBy(() -> CalculsGeometriques.aire(-1.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("rayon");
    }

    @Test
    @DisplayName("message nomme le paramètre fautif : aire(-1.0, 4.0) contient 'largeur'")
    void message_contient_nom_parametre_largeur() {
        assertThatThrownBy(() -> CalculsGeometriques.aire(-1.0, 4.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("largeur");
    }

    @Test
    @DisplayName("cercle rayon 1 : aire ≈ π, périmètre ≈ 2π")
    void cercle_rayon_unite() {
        assertThat(CalculsGeometriques.aire(1.0))
            .isCloseTo(Math.PI, within(1e-9));
        assertThat(CalculsGeometriques.perimetre(1.0))
            .isCloseTo(2 * Math.PI, within(1e-9));
    }

    @Test
    @DisplayName("deux dimensions négatives : perimetre(-2.0, -3.0) → IAE sur la première rencontrée")
    void perimetre_deux_negatifs() {
        assertThatThrownBy(() -> CalculsGeometriques.perimetre(-2.0, -3.0))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
