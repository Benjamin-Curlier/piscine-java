package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.1.3 Membre.
 */
@DisplayName("Membre — tests publics")
class MembreTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        assertThat(s.getNom()).isEqualTo("Martin");
        assertThat(s.getNiveau()).isEqualTo("Confirmé");
        assertThat(s.getPointsDeVie()).isEqualTo(100);
    }

    @Test
    @DisplayName("subirDegats réduit les points de vie")
    void subir_degats() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        s.subirDegats(30);
        assertThat(s.getPointsDeVie()).isEqualTo(70);
    }

    @Test
    @DisplayName("soigner augmente les points de vie")
    void soigner() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        s.soigner(20);
        assertThat(s.getPointsDeVie()).isEqualTo(120);
    }

    @Test
    @DisplayName("estActif est vrai au départ")
    void est_actif() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        assertThat(s.estActif()).isTrue();
    }

    @Test
    @DisplayName("toString au format attendu")
    void representation() {
        assertThat(new Membre("Martin", "Confirmé", 100))
            .hasToString("Martin (Confirmé) - 100 PV");
    }
}
