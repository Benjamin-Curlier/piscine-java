package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.4.1 Coordonnees.
 */
@DisplayName("Coordonnees — tests publics")
class CoordonneesTest {

    @Test
    @DisplayName("accesseurs générés")
    void accesseurs() {
        Coordonnees c = new Coordonnees(1, 2);
        assertThat(c.x()).isEqualTo(1);
        assertThat(c.y()).isEqualTo(2);
    }

    @Test
    @DisplayName("equals généré (par valeurs)")
    void egalite() {
        assertThat(new Coordonnees(1, 2)).isEqualTo(new Coordonnees(1, 2));
    }

    @Test
    @DisplayName("normeCarree")
    void norme() {
        assertThat(new Coordonnees(3, 4).normeCarree()).isEqualTo(25);
    }

    @Test
    @DisplayName("translater renvoie les bonnes coordonnées")
    void translater() {
        Coordonnees c = new Coordonnees(1, 2).translater(3, -1);
        assertThat(c.x()).isEqualTo(4);
        assertThat(c.y()).isEqualTo(1);
    }
}
