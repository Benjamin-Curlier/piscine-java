package piscine.moulinette.console.trigger;

import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.git.RefUpdate;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionTriggerTest {

    @Test
    void push_sur_rendu_1_1_declenche_runGroup() {
        var git = new FakeGitClient();
        git.pushedRefs = List.of(new RefUpdate("refs/heads/rendu/1.1", "abc", "def"));
        MoulinetteRunner runner = (sg, repo) -> new GroupReport(sg,
            List.of(new ExoOutcome("1.1.1", true, ""),
                    new ExoOutcome("1.1.2", false, "compile: KO")),
            true, Paths.get("/tmp/report.md"));

        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);
        String out = trigger.onPushSucceeded(ctx);

        assertThat(out)
            .contains("rendu/1.1")
            .contains("1.1.1").contains("✓")
            .contains("1.1.2").contains("✗")
            .contains("report.md");
    }

    @Test
    void push_sur_main_ne_declenche_pas() {
        var git = new FakeGitClient();
        git.pushedRefs = List.of(new RefUpdate("refs/heads/main", "a", "b"));
        var trigger = new SubmissionTrigger((sg, repo) -> { throw new AssertionError("ne doit pas être appelé"); });
        var ctx = new ReplContext(Paths.get("/repo"), git, null, Mode.LOCAL);

        String out = trigger.onPushSucceeded(ctx);

        assertThat(out).isNullOrEmpty();
    }
}
