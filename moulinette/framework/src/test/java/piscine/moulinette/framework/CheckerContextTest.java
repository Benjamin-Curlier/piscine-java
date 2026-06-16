package piscine.moulinette.framework;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CheckerContextTest {

    @Test
    void expose_les_trois_chemins() {
        var ctx = new CheckerContext("1.1.1", Path.of("/ws/exo"), Path.of("/piscine/exo"));
        assertThat(ctx.exerciseId()).isEqualTo("1.1.1");
        assertThat(ctx.renduPath()).isEqualTo(Path.of("/ws/exo"));
        assertThat(ctx.exerciseRefPath()).isEqualTo(Path.of("/piscine/exo"));
    }

    @Test
    void refuse_exerciseId_vide() {
        assertThatThrownBy(() -> new CheckerContext(" ", Path.of("/a"), Path.of("/b")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void refuse_les_nulls() {
        assertThatThrownBy(() -> new CheckerContext("1.1.1", null, Path.of("/b")))
            .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CheckerContext("1.1.1", Path.of("/a"), null))
            .isInstanceOf(NullPointerException.class);
    }
}
