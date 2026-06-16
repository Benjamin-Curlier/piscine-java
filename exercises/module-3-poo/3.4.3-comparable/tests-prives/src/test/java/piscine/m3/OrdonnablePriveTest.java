package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.4.3.
 */
@DisplayName("Ordonnable — tests privés")
class OrdonnablePriveTest {

    @Test
    @DisplayName("priorités égales : comparerA nul")
    void egalite() {
        assertThat(new Dossier("A", 4).comparerA(new Dossier("B", 4))).isZero();
    }

    @Test
    @DisplayName("appel via le type interface")
    void via_interface() {
        Ordonnable o = new Dossier("A", 7);
        assertThat(o.comparerA(new Dossier("B", 2))).isPositive();
    }

    @Test
    @DisplayName("plusPrioritaire d'un tableau vide vaut null")
    void vide() {
        assertThat(Classement.plusPrioritaire(new Dossier[] {})).isNull();
    }

    @Test
    @DisplayName("plusPrioritaire à un seul élément")
    void singleton() {
        Dossier seul = new Dossier("Seul", 1);
        assertThat(Classement.plusPrioritaire(new Dossier[] { seul })).isSameAs(seul);
    }
}
