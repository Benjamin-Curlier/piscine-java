package etnc.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Suite MODÈLE. Couvre aire, périmètre et l'invariant. Tue les trois mutants :
 * {@code aire-en-somme}, {@code perimetre-sans-double}, {@code invariant-absent}.
 */
class RectangleTest {

    @Test
    void aireDUnRectangleValide() {
        assertThat(new Rectangle(3, 4).aire()).isEqualTo(12);
    }

    @Test
    void perimetreDUnRectangleValide() {
        assertThat(new Rectangle(3, 4).perimetre()).isEqualTo(14);
    }

    @Test
    void largeurNegativeEstCorrigeeAUn() {
        assertThat(new Rectangle(-5, 4).getLargeur()).isEqualTo(1);
    }

    @Test
    void largeurNulleEstCorrigeeAUn() {
        assertThat(new Rectangle(0, 7).getLargeur()).isEqualTo(1);
    }
}
