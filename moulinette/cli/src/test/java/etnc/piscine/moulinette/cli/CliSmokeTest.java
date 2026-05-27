package etnc.piscine.moulinette.cli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Test de fumée du module cli.
 */
class CliSmokeTest {

    @Test
    void main_withNoArgs_shouldNotThrow() {
        assertThatNoException().isThrownBy(() -> Main.main(new String[]{}));
    }

    @Test
    void main_withHelpFlag_shouldNotThrow() {
        assertThatNoException().isThrownBy(() -> Main.main(new String[]{"--help"}));
    }

    @Test
    void main_withShortHelpFlag_shouldNotThrow() {
        assertThatNoException().isThrownBy(() -> Main.main(new String[]{"-h"}));
    }
}
