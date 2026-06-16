package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.3.1 Forme — polymorphisme.
 */
@DisplayName("Forme — tests privés")
class FormePriveTest {

    @Test
    @DisplayName("decrire via le type de base appelle le bon aire()")
    void decrire_polymorphe() {
        Forme f = new Cercle(2.0);
        // aire = pi*4 = 12.566..., perimetre = 2*pi*2 = 12.566...
        assertThat(f.decrire()).isEqualTo("aire = 12.57, perimetre = 12.57");
    }

    @Test
    @DisplayName("rectangle carré")
    void rectangle_carre() {
        assertThat(new Rectangle(3.0, 3.0).aire()).isEqualTo(9.0, within(1e-9));
    }

    @Test
    @DisplayName("cercle de rayon 0")
    void cercle_nul() {
        assertThat(new Cercle(0.0).aire()).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("somme polymorphe des aires d'un tableau de formes")
    void tableau_de_formes() {
        Forme[] formes = { new Cercle(1.0), new Rectangle(2.0, 3.0) };
        double somme = 0;
        for (Forme f : formes) {
            somme += f.aire();
        }
        assertThat(somme).isEqualTo(Math.PI + 6.0, within(1e-9));
    }
}
