package etnc.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Suite MODÈLE. Couvre les quatre opérations et la division par zéro. Tue les trois mutants :
 * {@code addition-en-soustraction}, {@code multiplication-en-addition},
 * {@code division-avale-le-zero} (qui renvoie au lieu de lever).
 */
class CalculatriceTest {

    private final Calculatrice calc = new Calculatrice();

    @Test
    void additionneDeuxEntiers() {
        assertThat(calc.ajouter(2, 3)).isEqualTo(5);
    }

    @Test
    void soustraitDeuxEntiers() {
        assertThat(calc.soustraire(7, 4)).isEqualTo(3);
    }

    @Test
    void multiplieDeuxEntiers() {
        assertThat(calc.multiplier(6, 7)).isEqualTo(42);
    }

    @Test
    void diviseDeuxEntiers() {
        assertThat(calc.diviser(6, 2)).isEqualTo(3);
    }

    @Test
    void diviserParZeroLeveUneException() {
        assertThatThrownBy(() -> calc.diviser(6, 0))
            .isInstanceOf(ArithmeticException.class);
    }
}
