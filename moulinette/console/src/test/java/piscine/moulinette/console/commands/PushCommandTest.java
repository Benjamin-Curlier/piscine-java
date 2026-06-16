package piscine.moulinette.console.commands;

import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PushCommandTest {

    @Test
    void push_appelle_git_avec_porcelain() {
        var git = new FakeGitClient();
        git.stub("push --porcelain origin rendu/1.1", new GitResult(0, "ok", ""));
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new PushCommand(null).execute(ctx, List.of("origin", "rendu/1.1"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("push", "--porcelain", "origin", "rendu/1.1"));
    }

    @Test
    void push_sans_assez_d_args_renvoie_usage() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new PushCommand(null).execute(ctx, List.of("origin"));

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("usage");
        assertThat(git.calls).isEmpty();
    }
}
