package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.1.1 Point2D.
 */
@DisplayName("Point2D — tests publics")
class Point2DTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        Point2D p = new Point2D(1.0, 2.0);
        assertThat(p.getX()).isEqualTo(1.0);
        assertThat(p.getY()).isEqualTo(2.0);
    }

    @Test
    @DisplayName("deplacer translate les coordonnees")
    void deplacer() {
        Point2D p = new Point2D(1.0, 2.0);
        p.deplacer(3.0, -1.0);
        assertThat(p.getX()).isEqualTo(4.0);
        assertThat(p.getY()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("distance euclidienne (triangle 3-4-5)")
    void distance() {
        Point2D origine = new Point2D(0.0, 0.0);
        Point2D cible = new Point2D(3.0, 4.0);
        assertThat(origine.distance(cible)).isEqualTo(5.0, within(1e-9));
    }

    @Test
    @DisplayName("toString au format (x, y)")
    void representation() {
        assertThat(new Point2D(1.0, 2.0)).hasToString("(1.0, 2.0)");
    }
}
