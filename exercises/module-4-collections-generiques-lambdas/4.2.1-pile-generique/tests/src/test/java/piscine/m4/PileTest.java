package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.1 (pile générique LIFO).
 */
@DisplayName("Pile — tests publics")
class PileTest {

    @Test
    @DisplayName("Pile<String> : ordre LIFO sur depiler (affectation sans cast)")
    void depiler_lifo_string() {
        Pile<String> pile = new Pile<>();
        pile.empiler("a");
        pile.empiler("b");
        String x = pile.depiler();
        String y = pile.depiler();
        assertThat(x).isEqualTo("b");
        assertThat(y).isEqualTo("a");
    }

    @Test
    @DisplayName("Pile<Integer> : généricité prouvée par compilation (sans cast)")
    void depiler_lifo_integer() {
        Pile<Integer> pile = new Pile<>();
        pile.empiler(1);
        pile.empiler(2);
        Integer n = pile.depiler();
        Integer m = pile.depiler();
        assertThat(n).isEqualTo(2);
        assertThat(m).isEqualTo(1);
    }

    @Test
    @DisplayName("sommet ne change pas la taille")
    void sommet_ne_retire_pas() {
        Pile<String> pile = new Pile<>();
        pile.empiler("x");
        pile.empiler("y");
        String haut = pile.sommet();
        assertThat(haut).isEqualTo("y");
        assertThat(pile.taille()).isEqualTo(2);
    }

    @Test
    @DisplayName("estVide et taille cohérents")
    void estvide_et_taille() {
        Pile<Integer> pile = new Pile<>();
        assertThat(pile.estVide()).isTrue();
        assertThat(pile.taille()).isZero();
        pile.empiler(7);
        assertThat(pile.estVide()).isFalse();
        assertThat(pile.taille()).isEqualTo(1);
    }
}
