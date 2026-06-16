package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.4.3 (interface de comparaison custom).
 */
@DisplayName("Ordonnable — tests publics")
class OrdonnableTest {

    @Test
    @DisplayName("accesseurs du dossier")
    void accesseurs() {
        Dossier d = new Dossier("Projet", 5);
        assertThat(d.getTitre()).isEqualTo("Projet");
        assertThat(d.getPriorite()).isEqualTo(5);
    }

    @Test
    @DisplayName("comparerA selon la priorité")
    void comparer() {
        Dossier haute = new Dossier("A", 5);
        Dossier basse = new Dossier("B", 3);
        assertThat(haute.comparerA(basse)).isPositive();
        assertThat(basse.comparerA(haute)).isNegative();
    }

    @Test
    @DisplayName("plusPrioritaire renvoie le dossier de plus haute priorité")
    void plus_prioritaire() {
        Dossier[] dossiers = { new Dossier("A", 3), new Dossier("B", 9), new Dossier("C", 5) };
        assertThat(Classement.plusPrioritaire(dossiers).getTitre()).isEqualTo("B");
    }
}
