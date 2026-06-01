package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommitCommandTest {

    @Test
    void commit_sans_m_renvoie_message_pedagogique() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new CommitCommand().execute(ctx, List.of());

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("-m").contains("message");
        assertThat(git.calls).isEmpty();
    }

    @Test
    void commit_avec_m_appelle_git() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        var r = new CommitCommand().execute(ctx, List.of("-m", "rendu 1.1.1"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("commit", "-m", "rendu 1.1.1"));
    }
}
