package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.2.1 Rectangle — invariants.
 */
@DisplayName("Rectangle — tests privés")
class RectanglePriveTest {

    @Test
    @DisplayName("constructeur : largeur <= 0 corrigée à 1.0")
    void largeur_corrigee() {
        Rectangle r = new Rectangle(-2.0, 4.0);
        assertThat(r.getLargeur()).isEqualTo(1.0, within(1e-9));
        assertThat(r.getHauteur()).isEqualTo(4.0, within(1e-9));
    }

    @Test
    @DisplayName("constructeur : hauteur <= 0 corrigée à 1.0")
    void hauteur_corrigee() {
        Rectangle r = new Rectangle(3.0, 0.0);
        assertThat(r.getHauteur()).isEqualTo(1.0, within(1e-9));
    }

    @Test
    @DisplayName("constructeur carré : cote <= 0 corrigé à 1.0 x 1.0")
    void carre_corrige() {
        Rectangle r = new Rectangle(-5.0);
        assertThat(r.getLargeur()).isEqualTo(1.0, within(1e-9));
        assertThat(r.getHauteur()).isEqualTo(1.0, within(1e-9));
    }

    @Test
    @DisplayName("setLargeur <= 0 : ignoré (objet inchangé)")
    void set_largeur_refuse() {
        Rectangle r = new Rectangle(3.0, 4.0);
        r.setLargeur(0.0);
        assertThat(r.getLargeur()).isEqualTo(3.0, within(1e-9));
        r.setLargeur(-1.0);
        assertThat(r.getLargeur()).isEqualTo(3.0, within(1e-9));
    }

    @Test
    @DisplayName("aire recalculée après setHauteur valide")
    void aire_apres_set() {
        Rectangle r = new Rectangle(3.0, 4.0);
        r.setHauteur(8.0);
        assertThat(r.aire()).isEqualTo(24.0, within(1e-9));
    }
}
