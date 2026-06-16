package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.2.2 (immuabilite, types homogenes, double inversion).
 */
@DisplayName("Paire — tests prives")
class PairePriveTest {

    @Test
    @DisplayName("inverser() ne modifie pas l'original (immuabilite)")
    void inverser_ne_modifie_pas_l_original() {
        Paire<String, Integer> p = new Paire<>("Mission", 5);
        Paire<Integer, String> q = p.inverser();
        assertThat(p.premier()).isEqualTo("Mission");
        assertThat(p.second()).isEqualTo(5);
        assertThat(q).isNotSameAs(p);
    }

    @Test
    @DisplayName("Paire<String, String> : les deux composants peuvent partager le type")
    void paire_de_meme_type() {
        Paire<String, String> p = new Paire<>("gauche", "droite");
        String a = p.premier();
        String b = p.second();
        assertThat(a).isEqualTo("gauche");
        assertThat(b).isEqualTo("droite");
    }

    @Test
    @DisplayName("inverser() deux fois redonne l'ordre initial")
    void double_inversion_redonne_l_ordre_initial() {
        Paire<String, Integer> p = new Paire<>("Mission", 5);
        Paire<String, Integer> deuxFois = p.inverser().inverser();
        assertThat(deuxFois.premier()).isEqualTo("Mission");
        assertThat(deuxFois.second()).isEqualTo(5);
    }
}
