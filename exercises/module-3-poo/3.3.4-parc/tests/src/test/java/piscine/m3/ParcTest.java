package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.3.4 Parc.
 */
@DisplayName("Parc — tests publics")
class ParcTest {

    @Test
    @DisplayName("surfaceTotale somme les aires")
    void surface_totale() {
        Forme[] formes = { new Cercle(1.0), new Rectangle(2.0, 3.0) };
        assertThat(Parc.surfaceTotale(formes)).isEqualTo(Math.PI + 6.0, within(1e-9));
    }

    @Test
    @DisplayName("plusGrande renvoie la forme d'aire maximale")
    void plus_grande() {
        Forme petit = new Cercle(1.0);          // aire pi ~ 3.14
        Forme grand = new Rectangle(2.0, 3.0);  // aire 6
        Forme[] formes = { petit, grand };
        assertThat(Parc.plusGrande(formes)).isSameAs(grand);
    }

    @Test
    @DisplayName("compterCercles compte les cercles")
    void compter_cercles() {
        Forme[] formes = { new Cercle(1.0), new Rectangle(2.0, 3.0), new Cercle(2.0) };
        assertThat(Parc.compterCercles(formes)).isEqualTo(2);
    }
}
