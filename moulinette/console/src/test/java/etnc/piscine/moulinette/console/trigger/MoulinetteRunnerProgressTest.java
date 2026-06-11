package etnc.piscine.moulinette.console.trigger;

import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;
import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
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

    /** Un exercices/ minimal (un exo 9.9.9) côté référence + un rendu vide côté repo. */
    private static ExerciseCatalog miniCatalog(Path tmp) throws IOException {
        Path exoRef = tmp.resolve("piscine/exercises/9.9.9-demo");
        Files.createDirectories(exoRef);
        Files.writeString(exoRef.resolve("metadata.yml"),
            "slug: demo\nmodule: 9\nsous_groupe: \"9.9\"\nposition: 1\nnotions: []\n");
        Files.createDirectories(tmp.resolve("repo/exercises/9.9.9-demo"));
        return ExerciseCatalog.scan(tmp.resolve("piscine/exercises"));
    }
}
