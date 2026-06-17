package piscine.moulinette.console.commands;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import piscine.moulinette.console.Mode;
import piscine.moulinette.console.git.FakeGitClient;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.workspace.ExerciseCatalog;

@DisplayName("profil — gamification visible")
class ProfilCommandTest {

    @Test
    @DisplayName("affiche XP, niveau et badges d'après progress.json")
    void affiche_le_profil(@TempDir Path tmp) throws IOException {
        Path ex = tmp.resolve("piscine/exercises");
        ecrireExo(ex, "9.9.1-a", 1, "facile"); // 15 XP
        ecrireExo(ex, "9.9.2-b", 1, "moyen");  // 25 XP
        ExerciseCatalog catalog = ExerciseCatalog.scan(ex);

        Path repo = tmp.resolve("repo");
        Files.createDirectories(repo.resolve(".piscine"));
        Files.writeString(repo.resolve(".piscine/progress.json"), "{\n  \"9.9.1\": true\n}\n");

        var ctx = new ReplContext(repo, new FakeGitClient(), catalog, Mode.LOCAL);
        CommandResult res = new ProfilCommand().execute(ctx, List.of());

        assertThat(res.exitCode()).isZero();
        assertThat(res.output())
            .contains("Niveau 1")
            .contains("15 XP")        // facile validé
            .contains("50%")          // 1 / 2 exercices
            .contains("Premier sang");
    }

    @Test
    @DisplayName("sans progression : 0 XP, aucun badge")
    void profil_vierge(@TempDir Path tmp) throws IOException {
        Path ex = tmp.resolve("piscine/exercises");
        ecrireExo(ex, "9.9.1-a", 1, "facile");
        ExerciseCatalog catalog = ExerciseCatalog.scan(ex);

        var ctx = new ReplContext(tmp.resolve("repo"), new FakeGitClient(), catalog, Mode.LOCAL);
        CommandResult res = new ProfilCommand().execute(ctx, List.of());

        assertThat(res.exitCode()).isZero();
        assertThat(res.output()).contains("0 XP").contains("Aucun badge");
    }

    private static void ecrireExo(Path base, String dir, int module, String difficulte) throws IOException {
        Path d = base.resolve(dir);
        Files.createDirectories(d);
        Files.writeString(d.resolve("metadata.yml"),
            "slug: " + dir + "\nmodule: " + module + "\nsous_groupe: \"9.9\"\nposition: 1\n"
            + "difficulte: " + difficulte + "\nnotions: []\n");
    }
}
