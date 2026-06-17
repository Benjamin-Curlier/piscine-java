package piscine.moulinette.console.trigger;

import piscine.moulinette.console.Mode;
import piscine.moulinette.console.gamification.Badge;
import piscine.moulinette.console.gamification.Profil;
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
            false, Paths.get("/tmp/report.md"));

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

    private static final Badge PREMIER_SANG =
        new Badge("premier-sang", "Premier sang", "Valider ton premier exercice.");
    private static final Badge ASSIDU =
        new Badge("assidu", "Assidu·e", "Valider 10 exercices.");

    @Test
    void resume_aucun_progres_rien_a_annoncer() {
        Profil avant = new Profil(25, 1, "Débutant", 100, 20, List.of(PREMIER_SANG));
        Profil apres = new Profil(25, 1, "Débutant", 100, 20, List.of(PREMIER_SANG));
        assertThat(SubmissionTrigger.resumeGamification(avant, apres)).isEmpty();
    }

    @Test
    void resume_profils_null_rien_a_annoncer() {
        assertThat(SubmissionTrigger.resumeGamification(null, null)).isEmpty();
    }

    @Test
    void resume_xp_simple_sans_niveau_ni_badge() {
        Profil avant = new Profil(10, 1, "Débutant", 100, 10, List.of(PREMIER_SANG));
        Profil apres = new Profil(25, 1, "Débutant", 100, 20, List.of(PREMIER_SANG));
        String s = SubmissionTrigger.resumeGamification(avant, apres);
        assertThat(s)
            .contains("+15 XP (total 25)")
            .contains("Tape `profil`")
            .doesNotContain("Niveau")
            .doesNotContain("Nouveau badge");
    }

    @Test
    void resume_montee_de_niveau_et_nouveau_badge() {
        Profil avant = new Profil(85, 1, "Débutant", 100, 40, List.of(PREMIER_SANG));
        Profil apres = new Profil(125, 2, "Apprenti", 250, 55, List.of(PREMIER_SANG, ASSIDU));
        String s = SubmissionTrigger.resumeGamification(avant, apres);
        assertThat(s)
            .contains("+40 XP (total 125)")
            .contains("Niveau 2 : Apprenti débloqué !")
            .contains("Nouveau badge : Assidu·e")
            .doesNotContain("Premier sang"); // badge déjà possédé, pas re-annoncé
    }
}
