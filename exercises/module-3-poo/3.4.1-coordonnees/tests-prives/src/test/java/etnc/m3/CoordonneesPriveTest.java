package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.4.1 Coordonnees — immuabilité.
 */
@DisplayName("Coordonnees — tests privés")
class CoordonneesPriveTest {

    @Test
    @DisplayName("translater n'altère pas l'original (immuabilité)")
    void immuabilite() {
        Coordonnees origine = new Coordonnees(1, 2);
        Coordonnees deplace = origine.translater(5, 5);
        assertThat(origine.x()).isEqualTo(1);
        assertThat(origine.y()).isEqualTo(2);
        assertThat(deplace).isNotSameAs(origine);
    }

    @Test
    @DisplayName("equals faux pour des valeurs différentes")
    void inegalite() {
        assertThat(new Coordonnees(1, 2)).isNotEqualTo(new Coordonnees(2, 1));
    }

    @Test
    @DisplayName("normeCarree avec coordonnées négatives")
    void norme_negative() {
        assertThat(new Coordonnees(-3, -4).normeCarree()).isEqualTo(25);
    }

    @Test
    @DisplayName("translater enchaîné")
    void translater_enchaine() {
        Coordonnees c = new Coordonnees(0, 0).translater(1, 1).translater(2, 3);
        assertThat(c).isEqualTo(new Coordonnees(3, 4));
    }
}
