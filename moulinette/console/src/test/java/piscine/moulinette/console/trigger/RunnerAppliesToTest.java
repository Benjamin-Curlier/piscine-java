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

import static org.assertj.core.api.Assertions.assertThat;

/** Le pipeline n'exécute que les checkers dont {@link Checker#appliesTo} est vrai pour l'exo. */
class RunnerAppliesToTest {

    /** Checker qui note s'il a été exécuté ; peut déclarer ne pas s'appliquer. */
    static final class SpyChecker implements Checker {
        final String id;
        final boolean applies;
        boolean checked = false;
        SpyChecker(String id, boolean applies) { this.id = id; this.applies = applies; }
        @Override public String id() { return id; }
        @Override public boolean appliesTo(CheckerContext ctx) { return applies; }
        @Override public CheckResult check(CheckerContext ctx) { checked = true; return CheckResult.ok(); }
    }

    @Test
    void un_checker_non_applicable_est_saute(@TempDir Path tmp) throws IOException {
        ExerciseCatalog catalog = miniCatalog(tmp);
        var applicable = new SpyChecker("applicable", true);
        var nonApplicable = new SpyChecker("non-applicable", false);

        var runner = new MoulinetteRunner.Default(catalog,
            java.util.List.of(applicable, nonApplicable), tmp.resolve("reports"));
        runner.runGroup("9.9", tmp.resolve("repo"));

        assertThat(applicable.checked).as("le checker applicable s'exécute").isTrue();
        assertThat(nonApplicable.checked).as("le checker non applicable est sauté").isFalse();
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
