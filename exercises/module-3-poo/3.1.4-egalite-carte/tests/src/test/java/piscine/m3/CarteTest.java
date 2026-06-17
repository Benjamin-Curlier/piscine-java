package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.1.4 (equals / hashCode et coherence HashSet).
 */
@DisplayName("Carte — tests publics")
class CarteTest {

    @Test
    @DisplayName("reflexivité : une carte est égale à elle-même")
    void reflexivite() {
        Carte c = new Carte(7, "Coeur");
        assertThat(c.equals(c)).isTrue();
    }

    @Test
    @DisplayName("égalité par valeur : même valeur et même couleur")
    void egalite_par_valeur() {
        Carte a = new Carte(7, "Coeur");
        Carte b = new Carte(7, "Coeur");
        assertThat(a).isEqualTo(b);
    }

    @Test
    @DisplayName("HashSet : deux cartes égales ne sont stockées qu'une fois")
    void hashset_deduplique() {
        Set<Carte> jeu = new HashSet<>();
        jeu.add(new Carte(7, "Coeur"));
        jeu.add(new Carte(7, "Coeur"));
        assertThat(jeu).hasSize(1);
    }
}
