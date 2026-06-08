package etnc.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * À COMPLÉTER. Un test vous est donné en modèle ; ajoutez les autres (voir sujet.md)
 * pour couvrir le périmètre ET l'invariant (dimension <= 0 corrigée à 1).
 */
class RectangleTest {

    @Test
    void aireDUnRectangleValide() {
        assertThat(new Rectangle(3, 4).aire()).isEqualTo(12);
    }

    // TODO : perimetre d'un Rectangle(3, 4) doit valoir 14.
    // TODO : new Rectangle(-5, 4).getLargeur() doit valoir 1 (invariant : dimension corrigée).
    // TODO : new Rectangle(0, 7).getLargeur() doit valoir 1.
}
