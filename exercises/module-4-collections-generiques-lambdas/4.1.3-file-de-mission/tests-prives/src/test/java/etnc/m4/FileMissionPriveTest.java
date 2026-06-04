package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 4.1.3 (cas limites file FIFO).
 */
@DisplayName("FileMission — tests privés")
class FileMissionPriveTest {

    @Test
    @DisplayName("traiterProchaine sur file vide renvoie null sans exception")
    void traiter_vide() {
        FileMission f = new FileMission();
        assertThat(f.traiterProchaine()).isNull();
    }

    @Test
    @DisplayName("prochaine sur file vide renvoie null sans exception")
    void prochaine_vide() {
        FileMission f = new FileMission();
        assertThat(f.prochaine()).isNull();
    }

    @Test
    @DisplayName("alternance ajout/retrait préserve l'ordre FIFO")
    void alternance_fifo() {
        FileMission f = new FileMission();
        f.ajouter("A");
        assertThat(f.traiterProchaine()).isEqualTo("A");
        f.ajouter("B");
        f.ajouter("C");
        assertThat(f.traiterProchaine()).isEqualTo("B");
        assertThat(f.prochaine()).isEqualTo("C");
        assertThat(f.taille()).isEqualTo(1);
    }
}
