package etnc.m1;

import etnc.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Tests publics de l'exercice 1.1.1 Hello World.
 *
 * Ces tests sont visibles par le stagiaire. La moulinette executera en plus
 * des tests prives (dans le dossier tests-prives/).
 */
@DisplayName("Hello World — tests publics")
class HelloWorldTest {

    @Test
    @DisplayName("affiche exactement 'Hello, world!' suivi d'un retour ligne")
    void affiche_hello_world_avec_retour_ligne() {
        String sortie = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));

        assertThat(sortie)
            .as("La sortie doit etre exactement 'Hello, world!' suivi d'un retour a la ligne")
            .isEqualTo("Hello, world!" + System.lineSeparator());
    }

    @Test
    @DisplayName("ne leve pas d'exception")
    void ne_leve_pas_d_exception() {
        assertThatNoException()
            .as("main(...) doit s'executer proprement, sans exception")
            .isThrownBy(() -> HelloWorld.main(new String[]{}));
    }
}
