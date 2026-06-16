package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.3 (file de taches FIFO).
 */
@DisplayName("FileTaches — tests publics")
class FileTachesTest {

    @Test
    @DisplayName("ordre FIFO : la première tâche ajoutée est la première traitée")
    void ordre_fifo() {
        FileTaches f = new FileTaches();
        f.ajouter("A");
        f.ajouter("B");
        f.ajouter("C");
        assertThat(f.traiterProchaine()).isEqualTo("A");
        assertThat(f.traiterProchaine()).isEqualTo("B");
    }

    @Test
    @DisplayName("prochaine renvoie la tête sans modifier la taille")
    void prochaine_ne_retire_pas() {
        FileTaches f = new FileTaches();
        f.ajouter("Alpha");
        f.ajouter("Bravo");
        assertThat(f.prochaine()).isEqualTo("Alpha");
        assertThat(f.taille()).isEqualTo(2);
    }

    @Test
    @DisplayName("estVide est vrai au départ puis faux après un ajout")
    void est_vide() {
        FileTaches f = new FileTaches();
        assertThat(f.estVide()).isTrue();
        f.ajouter("Tache");
        assertThat(f.estVide()).isFalse();
    }

    @Test
    @DisplayName("taille reflète le nombre de tâches en attente")
    void taille() {
        FileTaches f = new FileTaches();
        assertThat(f.taille()).isEqualTo(0);
        f.ajouter("X");
        f.ajouter("Y");
        assertThat(f.taille()).isEqualTo(2);
        f.traiterProchaine();
        assertThat(f.taille()).isEqualTo(1);
    }
}
