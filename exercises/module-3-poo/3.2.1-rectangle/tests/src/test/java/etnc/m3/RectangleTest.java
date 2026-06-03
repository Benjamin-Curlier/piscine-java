package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.2.1 Rectangle.
 */
@DisplayName("Rectangle — tests publics")
class RectangleTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        Rectangle r = new Rectangle(3.0, 4.0);
        assertThat(r.getLargeur()).isEqualTo(3.0, within(1e-9));
        assertThat(r.getHauteur()).isEqualTo(4.0, within(1e-9));
    }

    @Test
    @DisplayName("aire")
    void aire() {
        assertThat(new Rectangle(3.0, 4.0).aire()).isEqualTo(12.0, within(1e-9));
    }

    @Test
    @DisplayName("perimetre")
    void perimetre() {
        assertThat(new Rectangle(3.0, 4.0).perimetre()).isEqualTo(14.0, within(1e-9));
    }

    @Test
    @DisplayName("constructeur carré : largeur == hauteur")
    void carre() {
        Rectangle r = new Rectangle(5.0);
        assertThat(r.getLargeur()).isEqualTo(5.0, within(1e-9));
        assertThat(r.getHauteur()).isEqualTo(5.0, within(1e-9));
    }

    @Test
    @DisplayName("setLargeur avec une valeur valide")
    void set_largeur_valide() {
        Rectangle r = new Rectangle(3.0, 4.0);
        r.setLargeur(10.0);
        assertThat(r.getLargeur()).isEqualTo(10.0, within(1e-9));
    }

    @Test
    @DisplayName("toString au format attendu")
    void representation() {
        assertThat(new Rectangle(3.0, 4.0)).hasToString("Rectangle 3.0 x 4.0");
    }
}
