package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.1.4.
 */
@DisplayName("Carte — tests privés")
class CartePriveTest {

    @Test
    @DisplayName("symétrie : a.equals(b) <=> b.equals(a)")
    void symetrie() {
        Carte a = new Carte(10, "Pique");
        Carte b = new Carte(10, "Pique");
        assertThat(a.equals(b)).isTrue();
        assertThat(b.equals(a)).isTrue();
    }

    @Test
    @DisplayName("transitivité : a=b et b=c impliquent a=c")
    void transitivite() {
        Carte a = new Carte(3, "Trefle");
        Carte b = new Carte(3, "Trefle");
        Carte c = new Carte(3, "Trefle");
        assertThat(a.equals(b)).isTrue();
        assertThat(b.equals(c)).isTrue();
        assertThat(a.equals(c)).isTrue();
    }

    @Test
    @DisplayName("hashCode identique pour deux cartes égales")
    void hashcode_coherent() {
        Carte a = new Carte(1, "Carreau");
        Carte b = new Carte(1, "Carreau");
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("HashSet : taille après ajout de doublons et de cartes distinctes")
    void hashset_taille() {
        Set<Carte> jeu = new HashSet<>();
        jeu.add(new Carte(7, "Coeur"));
        jeu.add(new Carte(7, "Coeur"));   // doublon
        jeu.add(new Carte(7, "Pique"));   // couleur differente
        jeu.add(new Carte(8, "Coeur"));   // valeur differente
        assertThat(jeu).hasSize(3);
    }

    @Test
    @DisplayName("contains : un HashSet retrouve une carte égale mais non identique")
    void contains_coherent() {
        Set<Carte> jeu = new HashSet<>();
        jeu.add(new Carte(12, "Pique"));
        assertThat(jeu.contains(new Carte(12, "Pique"))).isTrue();
        assertThat(jeu.contains(new Carte(12, "Coeur"))).isFalse();
    }

    @Test
    @DisplayName("inégalité : valeur ou couleur différente, null et autre type")
    void inegalite() {
        Carte ref = new Carte(5, "Coeur");
        assertThat(ref.equals(new Carte(6, "Coeur"))).isFalse();  // valeur differente
        assertThat(ref.equals(new Carte(5, "Pique"))).isFalse();  // couleur differente
        assertThat(ref.equals(null)).isFalse();                   // null
        assertThat(ref.equals("5 de Coeur")).isFalse();           // type different
    }
}
