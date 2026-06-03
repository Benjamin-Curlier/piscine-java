package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.3.1 Forme.
 */
@DisplayName("Forme — tests publics")
class FormeTest {

    @Test
    @DisplayName("cercle : aire et perimetre")
    void cercle() {
        Cercle c = new Cercle(2.0);
        assertThat(c.aire()).isEqualTo(Math.PI * 4.0, within(1e-9));
        assertThat(c.perimetre()).isEqualTo(2 * Math.PI * 2.0, within(1e-9));
    }

    @Test
    @DisplayName("rectangle : aire et perimetre")
    void rectangle() {
        Rectangle r = new Rectangle(3.0, 4.0);
        assertThat(r.aire()).isEqualTo(12.0, within(1e-9));
        assertThat(r.perimetre()).isEqualTo(14.0, within(1e-9));
    }

    @Test
    @DisplayName("usage polymorphe via le type Forme")
    void polymorphe() {
        Forme f = new Cercle(1.0);
        assertThat(f.aire()).isEqualTo(Math.PI, within(1e-9));
    }

    @Test
    @DisplayName("decrire d'un rectangle")
    void decrire() {
        assertThat(new Rectangle(3.0, 4.0).decrire())
            .isEqualTo("aire = 12.00, perimetre = 14.00");
    }
}
