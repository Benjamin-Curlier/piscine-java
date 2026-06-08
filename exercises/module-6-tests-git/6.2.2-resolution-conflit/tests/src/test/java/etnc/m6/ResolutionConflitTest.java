package etnc.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests publics : on monte un vrai conflit de fusion, puis on vérifie qu'il est résolu. */
class ResolutionConflitTest {

    @TempDir
    Path tmp;
    private GitCommandes git;
    private Path readme;

    @BeforeEach
    void monterUnConflit() throws IOException {
        git = new GitCommandes(tmp);
        readme = tmp.resolve("README.md");
        git.run("init");
        git.run("config", "user.email", "stagiaire@etnc.fr");
        git.run("config", "user.name", "Stagiaire ETNC");

        Files.writeString(readme, "fonctionnalités:\n- login\n", StandardCharsets.UTF_8);
        git.run("add", "README.md");
        git.run("commit", "-m", "Base");
        git.run("branch", "-M", "main");

        git.run("switch", "-c", "feature");
        Files.writeString(readme, "fonctionnalités:\n- login\n- export\n", StandardCharsets.UTF_8);
        git.run("commit", "-am", "Ajoute export");

        git.run("switch", "main");
        Files.writeString(readme, "fonctionnalités:\n- login\n- import\n", StandardCharsets.UTF_8);
        git.run("commit", "-am", "Ajoute import");

        git.tenter("merge", "feature"); // déclenche le conflit (n'échoue pas le test)
    }

    @Test
    void plusAucunMarqueurDeConflit() throws IOException {
        new ResolutionConflit().resoudreConflit(git);
        String contenu = Files.readString(readme, StandardCharsets.UTF_8);
        assertThat(contenu)
            .doesNotContain("<<<<<<<")
            .doesNotContain(">>>>>>>");
    }

    @Test
    void arbreDeTravailPropreApres() throws IOException {
        new ResolutionConflit().resoudreConflit(git);
        assertThat(git.run("status", "--porcelain").strip()).isEmpty();
    }
}
