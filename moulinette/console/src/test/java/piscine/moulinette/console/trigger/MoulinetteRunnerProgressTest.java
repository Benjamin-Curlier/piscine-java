package piscine.moulinette.console.trigger;

import piscine.moulinette.console.workspace.ExerciseCatalog;
import piscine.moulinette.framework.CheckResult;
import piscine.moulinette.framework.Checker;
import piscine.moulinette.framework.CheckerContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Le runner persiste la progression (exos validés) dans .piscine/progress.json. */
class MoulinetteRunnerProgressTest {

    static final class FixedChecker implements Checker {
        final boolean ok;
        FixedChecker(boolean ok) { this.ok = ok; }
        @Override public String id() { return "fixe"; }
        @Override public CheckResult check(CheckerContext ctx) {
            return ok ? CheckResult.ok() : CheckResult.fail("raté", "corrige");
        }
    }

    /** Échoue uniquement pour l'exo dont l'id est donné ; réussit pour les autres. */
    static final class EchoueSi implements Checker {
        final String exoQuiEchoue;
        EchoueSi(String exoQuiEchoue) { this.exoQuiEchoue = exoQuiEchoue; }
        @Override public String id() { return "fixe"; }
        @Override public CheckResult check(CheckerContext ctx) {
            return ctx.exerciseId().equals(exoQuiEchoue)
                ? CheckResult.fail("raté", "corrige") : CheckResult.ok();
        }
    }

    @Test
    void un_exo_valide_est_persiste_dans_progress_json(@TempDir Path tmp) throws IOException {
        ExerciseCatalog catalog = miniCatalog(tmp);
        var runner = new MoulinetteRunner.Default(catalog,
            List.of(new FixedChecker(true)), tmp.resolve("reports"));

        runner.runGroup("9.9", tmp.resolve("repo"));

        Path progress = tmp.resolve("repo/.piscine/progress.json");
        assertThat(progress).exists();
        assertThat(Files.readString(progress)).contains("\"9.9.9\"").contains("true");
    }

    @Test
    void un_echec_ulterieur_ne_retrograde_pas_un_exo_valide(@TempDir Path tmp) throws IOException {
        ExerciseCatalog catalog = miniCatalog(tmp);
        Path repo = tmp.resolve("repo");

        new MoulinetteRunner.Default(catalog, List.of(new FixedChecker(true)), tmp.resolve("reports"))
            .runGroup("9.9", repo);
        new MoulinetteRunner.Default(catalog, List.of(new FixedChecker(false)), tmp.resolve("reports"))
            .runGroup("9.9", repo);

        assertThat(Files.readString(repo.resolve(".piscine/progress.json")))
            .contains("\"9.9.9\"").contains("true");
    }

    @Test
    void un_exo_en_echec_jamais_valide_nest_pas_marque(@TempDir Path tmp) throws IOException {
        ExerciseCatalog catalog = miniCatalog(tmp);
        var runner = new MoulinetteRunner.Default(catalog,
            List.of(new FixedChecker(false)), tmp.resolve("reports"));

        runner.runGroup("9.9", tmp.resolve("repo"));

        assertThat(Files.readString(tmp.resolve("repo/.piscine/progress.json")))
            .doesNotContain("\"9.9.9\": true");
    }

    @Test
    void tous_les_exos_du_groupe_sont_notes_meme_apres_un_echec(@TempDir Path tmp) throws IOException {
        ExerciseCatalog catalog = miniCatalog2(tmp); // 9.9.1-a (échoue), 9.9.2-b (passe)
        Path repo = tmp.resolve("repo");
        var runner = new MoulinetteRunner.Default(catalog,
            List.of(new EchoueSi("9.9.1")), tmp.resolve("reports"));

        var rep = runner.runGroup("9.9", repo);

        // grade-all : les DEUX exos sont notés (plus d'arrêt au premier échec).
        assertThat(rep.outcomes()).hasSize(2);
        assertThat(rep.tousReussis()).isFalse();
        // Progression séquentielle : 9.9.2 réussi « en avance » n'est PAS validé tant que 9.9.1 échoue.
        String progress = Files.readString(repo.resolve(".piscine/progress.json"));
        assertThat(progress).doesNotContain("9.9.1").doesNotContain("9.9.2");
    }

    /** Un exercices/ minimal (un exo 9.9.9) côté référence + un rendu vide côté repo. */
    private static ExerciseCatalog miniCatalog(Path tmp) throws IOException {
        Path exoRef = tmp.resolve("piscine/exercises/9.9.9-demo");
        Files.createDirectories(exoRef);
        Files.writeString(exoRef.resolve("metadata.yml"),
            "slug: demo\nmodule: 9\nsous_groupe: \"9.9\"\nposition: 1\nnotions: []\n");
        Files.createDirectories(tmp.resolve("repo/exercises/9.9.9-demo"));
        return ExerciseCatalog.scan(tmp.resolve("piscine/exercises"));
    }

    /** Deux exos dans le sous-groupe 9.9 : 9.9.1-a (position 1) et 9.9.2-b (position 2). */
    private static ExerciseCatalog miniCatalog2(Path tmp) throws IOException {
        Path base = tmp.resolve("piscine/exercises");
        creerExo(base, "9.9.1-a", 1);
        creerExo(base, "9.9.2-b", 2);
        Files.createDirectories(tmp.resolve("repo/exercises/9.9.1-a"));
        Files.createDirectories(tmp.resolve("repo/exercises/9.9.2-b"));
        return ExerciseCatalog.scan(base);
    }

    private static void creerExo(Path base, String dir, int position) throws IOException {
        Path d = base.resolve(dir);
        Files.createDirectories(d);
        Files.writeString(d.resolve("metadata.yml"),
            "slug: " + dir + "\nmodule: 9\nsous_groupe: \"9.9\"\nposition: " + position + "\nnotions: []\n");
    }
}
