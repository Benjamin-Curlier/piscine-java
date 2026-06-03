package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.3.4 Parc — cas limites et downcast.
 */
@DisplayName("Parc — tests privés")
class ParcPriveTest {

    @Test
    @DisplayName("surfaceTotale d'un tableau vide vaut 0")
    void surface_vide() {
        assertThat(Parc.surfaceTotale(new Forme[] {})).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("plusGrande d'un tableau vide vaut null")
    void plus_grande_vide() {
        assertThat(Parc.plusGrande(new Forme[] {})).isNull();
    }

    @Test
    @DisplayName("compterCercles sans cercle vaut 0")
    void compter_sans_cercle() {
        Forme[] formes = { new Rectangle(1.0, 1.0), new Rectangle(2.0, 2.0) };
        assertThat(Parc.compterCercles(formes)).isZero();
    }

    @Test
    @DisplayName("plusGrande quand le maximum est un cercle, exploitable après downcast")
    void plus_grande_cercle_downcast() {
        Forme[] formes = { new Rectangle(1.0, 1.0), new Cercle(5.0) };
        Forme max = Parc.plusGrande(formes);
        assertThat(max).isInstanceOf(Cercle.class);
        Cercle c = (Cercle) max;
        assertThat(c.getRayon()).isEqualTo(5.0, within(1e-9));
    }
}
