package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;
import etnc.piscine.moulinette.console.git.FakeGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleSessionTest {

    private static ConsoleSession session(FakeGitClient git) {
        var ctx = new ReplContext(Paths.get("."), git, null, Mode.LOCAL);
        return ConsoleSession.of(ctx, CommandRegistry.defaults(null));
    }

    @Test
    void execute_dispatch_une_ligne_et_rend_le_resultat() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("help");
        assertThat(r.exitCode()).isZero();
        assertThat(r.output()).contains("Commandes supportées");
    }

    @Test
    void execute_tokenise_avec_guillemets_avant_dispatch() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("git commit -m \"mon message\"");
        assertThat(r.exitCode()).isZero();
        // le message entre guillemets arrive au client git comme un seul argument
        assertThat(git.calls).anySatisfy(call ->
            assertThat(call).containsSubsequence("commit", "-m", "mon message"));
    }

    @Test
    void execute_commande_inconnue_rend_une_erreur_sans_lever() {
        var git = new FakeGitClient();
        CommandResult r = session(git).execute("git checkout main");
        assertThat(r.exitCode()).isNotZero();
        assertThat(r.output()).contains("non supportée");
    }

    @Test
    void currentBranch_delegue_au_contexte() {
        var git = new FakeGitClient();
        git.branch = "rendu/1.1";
        assertThat(session(git).currentBranch()).isEqualTo("rendu/1.1");
    }

    @Test
    void tokenize_respecte_les_guillemets() {
        assertThat(ConsoleSession.tokenize("git commit -m \"mon message\""))
            .containsExactly("git", "commit", "-m", "mon message");
        assertThat(ConsoleSession.tokenize("git add exercices/1.1.1"))
            .containsExactly("git", "add", "exercices/1.1.1");
    }
}
