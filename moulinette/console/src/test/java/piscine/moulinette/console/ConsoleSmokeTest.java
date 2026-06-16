package piscine.moulinette.console;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ConsoleSmokeTest {

    @Test
    void le_module_console_est_chargeable() {
        assertThat(Mode.values()).containsExactly(Mode.LOCAL);
    }
}
