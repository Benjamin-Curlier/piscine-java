package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 4.2.1.
 */
@DisplayName("Pile — tests privés")
class PilePriveTest {

    @Test
    @DisplayName("depiler sur pile vide renvoie null (pas d'exception)")
    void depiler_vide_null() {
        Pile<String> pile = new Pile<>();
        assertThat(pile.depiler()).isNull();
    }

    @Test
    @DisplayName("sommet sur pile vide renvoie null (pas d'exception)")
    void sommet_vide_null() {
        Pile<Integer> pile = new Pile<>();
        assertThat(pile.sommet()).isNull();
    }

    @Test
    @DisplayName("empiler puis tout dépiler : estVide redevient true")
    void tout_depiler_vide() {
        Pile<String> pile = new Pile<>();
        pile.empiler("a");
        pile.empiler("b");
        pile.depiler();
        pile.depiler();
        assertThat(pile.estVide()).isTrue();
        assertThat(pile.taille()).isZero();
    }

    @Test
    @DisplayName("ordre LIFO sur une séquence de 5 éléments")
    void lifo_cinq_elements() {
        Pile<Integer> pile = new Pile<>();
        pile.empiler(10);
        pile.empiler(20);
        pile.empiler(30);
        pile.empiler(40);
        pile.empiler(50);
        assertThat(pile.depiler()).isEqualTo(50);
        assertThat(pile.depiler()).isEqualTo(40);
        assertThat(pile.depiler()).isEqualTo(30);
        assertThat(pile.depiler()).isEqualTo(20);
        assertThat(pile.depiler()).isEqualTo(10);
        assertThat(pile.estVide()).isTrue();
    }
}
