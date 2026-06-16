package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.1.1 Point2D — cas limites.
 */
@DisplayName("Point2D — tests privés")
class Point2DPriveTest {

    @Test
    @DisplayName("distance d'un point à lui-même = 0")
    void distance_nulle() {
        Point2D p = new Point2D(2.5, -3.0);
        assertThat(p.distance(p)).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("coordonnées négatives")
    void coordonnees_negatives() {
        Point2D p = new Point2D(-1.0, -2.0);
        assertThat(p.getX()).isEqualTo(-1.0);
        assertThat(p.getY()).isEqualTo(-2.0);
    }

    @Test
    @DisplayName("deplacer cumulé sur deux appels")
    void deplacer_cumule() {
        Point2D p = new Point2D(0.0, 0.0);
        p.deplacer(1.0, 1.0);
        p.deplacer(2.0, -3.0);
        assertThat(p.getX()).isEqualTo(3.0);
        assertThat(p.getY()).isEqualTo(-2.0);
    }

    @Test
    @DisplayName("distance symétrique a->b == b->a")
    void distance_symetrique() {
        Point2D a = new Point2D(1.0, 1.0);
        Point2D b = new Point2D(4.0, 5.0);
        assertThat(a.distance(b)).isEqualTo(b.distance(a), within(1e-9));
    }

    @Test
    @DisplayName("toString après deplacer")
    void toString_apres_deplacer() {
        Point2D p = new Point2D(1.0, 1.0);
        p.deplacer(0.5, 0.5);
        assertThat(p).hasToString("(1.5, 1.5)");
    }
}
