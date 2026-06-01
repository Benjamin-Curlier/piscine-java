package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AddCommandTest {

    @Test
    void add_appelle_git_avec_les_bons_args() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of("file1.txt", "dir/"));

        assertThat(r.exitCode()).isZero();
        assertThat(git.calls).containsExactly(List.of("add", "file1.txt", "dir/"));
    }

    @Test
    void add_sans_arg_renvoie_message_pedagogique() {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of());

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("usage").contains("git add");
        assertThat(git.calls).isEmpty();
    }

    @Test
    void add_propage_l_echec_git() {
        var git = new FakeGitClient();
        git.stub("add inconnu.txt", new GitResult(128, "", "pathspec 'inconnu.txt' did not match"));
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);

        CommandResult r = new AddCommand().execute(ctx, List.of("inconnu.txt"));

        assertThat(r.exitCode()).isOne();
        assertThat(r.output()).contains("pathspec");
    }
}
