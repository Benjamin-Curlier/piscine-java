package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.ConsoleSession;
import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ReplTest {

    private static ConsoleSession session(FakeGitClient git) {
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        return ConsoleSession.of(ctx, CommandRegistry.defaults(null));
    }

    @Test
    void boucle_lit_dispatch_affiche_puis_quitte_sur_exit() throws Exception {
        var git = new FakeGitClient();
        git.branch = "main";
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader("help\nexit\n"), sw);

        new Repl(session(git), io).run();

        String out = sw.toString();
        assertThat(out).contains("piscine[main]>").contains("Commandes supportées");
    }

    @Test
    void commande_inconnue_affiche_message_et_continue() throws Exception {
        var git = new FakeGitClient();
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader("git checkout main\nexit\n"), sw);

        new Repl(session(git), io).run();

        assertThat(sw.toString()).contains("non supportée");
    }
}
