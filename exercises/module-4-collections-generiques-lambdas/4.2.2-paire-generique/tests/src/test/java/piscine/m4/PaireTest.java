package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.2 (paire generique a deux parametres de type).
 *
 * <p>La genericite est prouvee par la compilation : les affectations sans cast
 * (String s = ... ; Integer i = ...) n'existent que si &lt;A, B&gt; est propage.</p>
 */
@DisplayName("Paire — tests publics")
class PaireTest {

    @Test
    @DisplayName("premier()/second() : bons types et bonnes valeurs sans cast")
    void accesseurs_types_et_valeurs() {
        Paire<String, Integer> p = new Paire<>("Mission", 5);
        String s = p.premier();
        Integer i = p.second();
        assertThat(s).isEqualTo("Mission");
        assertThat(i).isEqualTo(5);
    }

    @Test
    @DisplayName("inverser() renvoie une Paire<Integer, String> aux composants echanges")
    void inverser_echange_les_composants() {
        Paire<String, Integer> p = new Paire<>("Mission", 5);
        Paire<Integer, String> q = p.inverser();
        Integer premier = q.premier();
        String second = q.second();
        assertThat(premier).isEqualTo(5);
        assertThat(second).isEqualTo("Mission");
    }

    @Test
    @DisplayName("de(\"x\", 1) infere les types et renvoie une paire correcte")
    void fabrique_infere_les_types() {
        Paire<String, Integer> r = Paire.de("x", 1);
        String s = r.premier();
        Integer i = r.second();
        assertThat(s).isEqualTo("x");
        assertThat(i).isEqualTo(1);
    }

    @Test
    @DisplayName("de fonctionne avec un autre couple de types (Integer, String)")
    void fabrique_autre_couple_de_types() {
        Paire<Integer, String> r = Paire.de(42, "reponse");
        Integer i = r.premier();
        String s = r.second();
        assertThat(i).isEqualTo(42);
        assertThat(s).isEqualTo("reponse");
    }
}
