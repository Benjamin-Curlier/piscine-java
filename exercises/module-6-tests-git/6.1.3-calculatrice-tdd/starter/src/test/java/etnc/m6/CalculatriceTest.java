package etnc.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * À COMPLÉTER. Deux tests vous sont donnés en modèle ; ajoutez les autres (voir sujet.md)
 * pour couvrir les quatre opérations ET la division par zéro qui doit lever.
 */
class CalculatriceTest {

    private final Calculatrice calc = new Calculatrice();

    @Test
    void additionneDeuxEntiers() {
        assertThat(calc.ajouter(2, 3)).isEqualTo(5);
    }

    @Test
    void diviserParZeroLeveUneException() {
        assertThatThrownBy(() -> calc.diviser(6, 0))
            .isInstanceOf(ArithmeticException.class);
    }

    // TODO : soustraire(7, 4) == 3
    // TODO : multiplier(6, 7) == 42
    // TODO : diviser(6, 2) == 3
}
