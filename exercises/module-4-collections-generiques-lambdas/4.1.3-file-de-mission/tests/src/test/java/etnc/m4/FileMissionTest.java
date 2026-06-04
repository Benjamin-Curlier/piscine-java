package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.3 (file de missions FIFO).
 */
@DisplayName("FileMission — tests publics")
class FileMissionTest {

    @Test
    @DisplayName("ordre FIFO : la première mission ajoutée est la première traitée")
    void ordre_fifo() {
        FileMission f = new FileMission();
        f.ajouter("A");
        f.ajouter("B");
        f.ajouter("C");
        assertThat(f.traiterProchaine()).isEqualTo("A");
        assertThat(f.traiterProchaine()).isEqualTo("B");
    }

    @Test
    @DisplayName("prochaine renvoie la tête sans modifier la taille")
    void prochaine_ne_retire_pas() {
        FileMission f = new FileMission();
        f.ajouter("Alpha");
        f.ajouter("Bravo");
        assertThat(f.prochaine()).isEqualTo("Alpha");
        assertThat(f.taille()).isEqualTo(2);
    }

    @Test
    @DisplayName("estVide est vrai au départ puis faux après un ajout")
    void est_vide() {
        FileMission f = new FileMission();
        assertThat(f.estVide()).isTrue();
        f.ajouter("Mission");
        assertThat(f.estVide()).isFalse();
    }

    @Test
    @DisplayName("taille reflète le nombre de missions en attente")
    void taille() {
        FileMission f = new FileMission();
        assertThat(f.taille()).isEqualTo(0);
        f.ajouter("X");
        f.ajouter("Y");
        assertThat(f.taille()).isEqualTo(2);
        f.traiterProchaine();
        assertThat(f.taille()).isEqualTo(1);
    }
}
