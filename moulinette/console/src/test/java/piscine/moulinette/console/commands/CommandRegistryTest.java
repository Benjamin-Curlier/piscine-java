package piscine.moulinette.console.commands;

import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandRegistryTest {

    private final ReplContext ctx = new ReplContext(Paths.get("."), new FakeGitClient(), null, Mode.LOCAL);

    @Test
    void dispatch_git_add_resout_add_command() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of("git", "add", "f.txt"));
        assertThat(r.exitCode()).isZero();
    }

    @Test
    void commande_inconnue_retourne_message_avec_suggestion() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of("git", "checkout", "-b", "x"));
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("non supportée").contains("submit-start");
    }

    @Test
    void ligne_vide_est_no_op() {
        var reg = CommandRegistry.defaults(null);
        var r = reg.dispatch(ctx, List.of());
        assertThat(r.exitCode()).isZero();
    }
}
