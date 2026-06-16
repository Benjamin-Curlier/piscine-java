package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.1.1 (annuaire de l'equipe — cas limites).
 */
@DisplayName("Annuaire — tests prives")
class AnnuairePriveTest {

    @Test
    @DisplayName("supprimer un indicatif absent renvoie false sans exception")
    void supprimer_absent_renvoie_false() {
        Annuaire a = new Annuaire();
        assertThat(a.supprimer("INCONNU-99")).isFalse();
    }

    @Test
    @DisplayName("annuaire vide : taille 0 et indicatifsTries est vide")
    void annuaire_vide() {
        Annuaire a = new Annuaire();
        assertThat(a.taille()).isEqualTo(0);
        assertThat(a.indicatifsTries()).isEmpty();
    }

    @Test
    @DisplayName("supprimer un present renvoie true puis rechercher renvoie Inconnu et taille decrementee")
    void supprimer_present() {
        Annuaire a = new Annuaire();
        a.enregistrer("ALPHA-7", "Martin");
        a.enregistrer("BRAVO-1", "Leclerc");
        assertThat(a.supprimer("ALPHA-7")).isTrue();
        assertThat(a.rechercher("ALPHA-7")).isEqualTo("Inconnu");
        assertThat(a.taille()).isEqualTo(1);
    }
}
