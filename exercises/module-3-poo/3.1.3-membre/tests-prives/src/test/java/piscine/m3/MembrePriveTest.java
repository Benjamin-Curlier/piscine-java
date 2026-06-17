package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.1.3 Membre — cas limites.
 */
@DisplayName("Membre — tests privés")
class MembrePriveTest {

    @Test
    @DisplayName("subirDegats au-delà des PV : plancher à 0 et estActif faux")
    void degats_critiques() {
        Membre s = new Membre("Martin", "Confirmé", 50);
        s.subirDegats(80);
        assertThat(s.getPointsDeVie()).isEqualTo(0);
        assertThat(s.estActif()).isFalse();
    }

    @Test
    @DisplayName("soigner après avoir subi des dégâts")
    void soigner_apres_degats() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        s.subirDegats(40);
        s.soigner(10);
        assertThat(s.getPointsDeVie()).isEqualTo(70);
    }

    @Test
    @DisplayName("dégâts cumulés sur deux appels")
    void degats_cumules() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        s.subirDegats(30);
        s.subirDegats(30);
        assertThat(s.getPointsDeVie()).isEqualTo(40);
    }

    @Test
    @DisplayName("estActif faux à exactement 0 PV")
    void inactif_a_zero() {
        Membre s = new Membre("Martin", "Confirmé", 10);
        s.subirDegats(10);
        assertThat(s.getPointsDeVie()).isEqualTo(0);
        assertThat(s.estActif()).isFalse();
    }

    @Test
    @DisplayName("toString après dégâts")
    void toString_apres_degats() {
        Membre s = new Membre("Martin", "Confirmé", 100);
        s.subirDegats(50);
        assertThat(s).hasToString("Martin (Confirmé) - 50 PV");
    }
}
