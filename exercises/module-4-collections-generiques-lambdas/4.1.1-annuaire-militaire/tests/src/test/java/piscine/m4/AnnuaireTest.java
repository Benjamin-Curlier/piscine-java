package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.1 (annuaire de l'equipe — Map).
 */
@DisplayName("Annuaire — tests publics")
class AnnuaireTest {

    @Test
    @DisplayName("enregistrer puis rechercher renvoie le nom")
    void enregistrer_puis_rechercher() {
        Annuaire a = new Annuaire();
        a.enregistrer("ALPHA-7", "Martin");
        assertThat(a.rechercher("ALPHA-7")).isEqualTo("Martin");
    }

    @Test
    @DisplayName("rechercher un indicatif absent renvoie Inconnu")
    void rechercher_absent() {
        Annuaire a = new Annuaire();
        assertThat(a.rechercher("DELTA-9")).isEqualTo("Inconnu");
    }

    @Test
    @DisplayName("reenregistrer un indicatif existant ecrase l'ancien nom")
    void reenregistrer_ecrase() {
        Annuaire a = new Annuaire();
        a.enregistrer("BRAVO-1", "Leclerc");
        a.enregistrer("BRAVO-1", "Dupont");
        assertThat(a.rechercher("BRAVO-1")).isEqualTo("Dupont");
    }

    @Test
    @DisplayName("taille apres 2 ajouts vaut 2")
    void taille_apres_deux_ajouts() {
        Annuaire a = new Annuaire();
        a.enregistrer("ALPHA-7", "Martin");
        a.enregistrer("BRAVO-1", "Leclerc");
        assertThat(a.taille()).isEqualTo(2);
    }

    @Test
    @DisplayName("indicatifsTries renvoie les indicatifs dans l'ordre alphabetique")
    void indicatifs_tries_ordre_alpha() {
        Annuaire a = new Annuaire();
        a.enregistrer("CHARLIE-3", "Dupont");
        a.enregistrer("ALPHA-7", "Martin");
        a.enregistrer("BRAVO-1", "Leclerc");
        assertThat(a.indicatifsTries()).containsExactly("ALPHA-7", "BRAVO-1", "CHARLIE-3");
    }
}
