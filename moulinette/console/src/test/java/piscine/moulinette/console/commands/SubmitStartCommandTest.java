package piscine.moulinette.console.commands;

import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubmitStartCommandTest {

    @Test
    void cree_et_bascule_sur_branche_rendu() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of("1.1"));
        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("checkout", "-B", "rendu/1.1"));
    }

    @Test
    void sans_argument_renvoie_usage() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of());
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("usage").contains("1.1");
    }

    @Test
    void format_de_groupe_invalide_renvoie_message_clair() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var r = new SubmitStartCommand().execute(ctx, List.of("xyz"));
        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("format");
    }
}
