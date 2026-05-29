package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ReplTest {

    @Test
    void boucle_lit_dispatch_affiche_puis_quitte_sur_exit() throws Exception {
        var git = new FakeGitClient();
        git.branch = "main";
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader("help\nexit\n"), sw);

        new Repl(ctx, CommandRegistry.defaults(null), io).run();

        String out = sw.toString();
        assertThat(out).contains("piscine[main]>").contains("Commandes supportées");
    }

    @Test
    void commande_inconnue_affiche_message_et_continue() throws Exception {
        var git = new FakeGitClient();
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader("git checkout main\nexit\n"), sw);

        new Repl(ctx, CommandRegistry.defaults(null), io).run();

        assertThat(sw.toString()).contains("non supportée");
    }

    @Test
    void tokenize_respecte_les_guillemets() {
        assertThat(Repl.tokenize("git commit -m \"mon message\""))
            .containsExactly("git", "commit", "-m", "mon message");
        assertThat(Repl.tokenize("git add exercices/1.1.1"))
            .containsExactly("git", "add", "exercices/1.1.1");
    }
}
