package piscine.m1;

import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.1.1 Hello World.
 *
 * Executes uniquement par la moulinette. Le stagiaire ne voit pas leur contenu
 * avant d'avoir rendu sa solution. Couvrent des cas que les tests publics
 * ne verifient pas explicitement.
 */
@DisplayName("Hello World — tests prives")
class HelloWorldPriveTest {

    @Test
    @DisplayName("ignore les arguments de ligne de commande")
    void ignore_les_arguments() {
        String sortie = CaptureSortie.capturer(
            () -> HelloWorld.main(new String[]{"foo", "bar", "baz"})
        );

        assertThat(sortie)
            .as("Le programme doit afficher la meme chose, qu'on lui passe des arguments ou non")
            .isEqualTo("Hello, world!" + System.lineSeparator());
    }

    @Test
    @DisplayName("execution repetee : pas de fuite d'etat entre deux appels")
    void execution_repetee_stable() {
        String premiereSortie = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));
        String secondeSortie = CaptureSortie.capturer(() -> HelloWorld.main(new String[]{}));

        assertThat(premiereSortie)
            .as("Deux executions consecutives doivent produire exactement la meme sortie")
            .isEqualTo(secondeSortie);
    }
}
