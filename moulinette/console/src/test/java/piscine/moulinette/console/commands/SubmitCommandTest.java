package piscine.moulinette.console.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.git.RefUpdate;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.MoulinetteRunner;
import piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;
import piscine.moulinette.console.trigger.SubmissionTrigger;

@DisplayName("submit — rendu en une commande")
class SubmitCommandTest {

    private static SubmissionTrigger triggerQuiNote(String... exos) {
        MoulinetteRunner runner = (sg, repo) -> {
            var outcomes = new java.util.ArrayList<ExoOutcome>();
            for (String e : exos) {
                outcomes.add(new ExoOutcome(e, true, ""));
            }
            return new GroupReport(sg, outcomes, true, Paths.get("/tmp/r.md"));
        };
        return new SubmissionTrigger(runner);
    }

    @Test
    @DisplayName("enchaîne checkout/add/commit/push et déclenche la moulinette")
    void enchaine_tout() {
        var git = new FakeGitClient();
        git.pushedRefs = List.of(new RefUpdate("refs/heads/rendu/1.1", "a", "b"));
        var cmd = new SubmitCommand(triggerQuiNote("1.1.1"));
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        CommandResult res = cmd.execute(ctx, List.of("1.1"));

        assertThat(res.exitCode()).isZero();
        assertThat(git.calls).contains(
            List.of("checkout", "-B", "rendu/1.1"),
            List.of("add", "-A"),
            List.of("commit", "-m", "rendu 1.1"),
            List.of("push", "--porcelain", "origin", "rendu/1.1"));
        assertThat(res.output()).contains("1.1.1").contains("✓");
    }

    @Test
    @DisplayName("sans argument, infère le sous-groupe depuis la branche rendu/ courante")
    void infere_depuis_la_branche() {
        var git = new FakeGitClient();
        git.branch = "rendu/2.3";
        git.pushedRefs = List.of(new RefUpdate("refs/heads/rendu/2.3", "a", "b"));
        var cmd = new SubmitCommand(triggerQuiNote());
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        CommandResult res = cmd.execute(ctx, List.of());

        assertThat(res.exitCode()).isZero();
        assertThat(git.calls).contains(List.of("checkout", "-B", "rendu/2.3"));
    }

    @Test
    @DisplayName("sans argument hors d'une branche rendu, explique l'usage")
    void usage_si_pas_de_groupe() {
        var git = new FakeGitClient();
        git.branch = "main";
        var cmd = new SubmitCommand(null);
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        CommandResult res = cmd.execute(ctx, List.of());

        assertThat(res.exitCode()).isEqualTo(1);
        assertThat(res.output()).contains("usage").contains("submit");
    }

    @Test
    @DisplayName("tolère « rien à valider » et pousse quand même")
    void tolere_rien_a_valider() {
        var git = new FakeGitClient();
        git.stub("commit -m rendu 1.1", new GitResult(1, "nothing to commit, working tree clean", ""));
        git.pushedRefs = List.of(new RefUpdate("refs/heads/rendu/1.1", "a", "b"));
        var cmd = new SubmitCommand(triggerQuiNote());
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        CommandResult res = cmd.execute(ctx, List.of("1.1"));

        assertThat(res.exitCode()).isZero();
        assertThat(git.calls).contains(List.of("push", "--porcelain", "origin", "rendu/1.1"));
    }
}
