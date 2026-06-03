package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.1.3 Soldat — cas limites.
 */
@DisplayName("Soldat — tests privés")
class SoldatPriveTest {

    @Test
    @DisplayName("subirDegats au-delà des PV : plancher à 0 et estVivant faux")
    void degats_mortels() {
        Soldat s = new Soldat("Martin", "Sergent", 50);
        s.subirDegats(80);
        assertThat(s.getPointsDeVie()).isEqualTo(0);
        assertThat(s.estVivant()).isFalse();
    }

    @Test
    @DisplayName("soigner après avoir subi des dégâts")
    void soigner_apres_degats() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        s.subirDegats(40);
        s.soigner(10);
        assertThat(s.getPointsDeVie()).isEqualTo(70);
    }

    @Test
    @DisplayName("dégâts cumulés sur deux appels")
    void degats_cumules() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        s.subirDegats(30);
        s.subirDegats(30);
        assertThat(s.getPointsDeVie()).isEqualTo(40);
    }

    @Test
    @DisplayName("estVivant faux à exactement 0 PV")
    void mort_a_zero() {
        Soldat s = new Soldat("Martin", "Sergent", 10);
        s.subirDegats(10);
        assertThat(s.getPointsDeVie()).isEqualTo(0);
        assertThat(s.estVivant()).isFalse();
    }

    @Test
    @DisplayName("toString après dégâts")
    void toString_apres_degats() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        s.subirDegats(50);
        assertThat(s).hasToString("Martin (Sergent) - 50 PV");
    }
}
