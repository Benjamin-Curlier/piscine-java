package piscine.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests privés : les deux contributions conservées, et la fusion conclue par un commit de merge. */
class ResolutionConflitPriveTest {

    @TempDir
    Path tmp;
    private GitCommandes git;
    private Path readme;

    @BeforeEach
    void monterUnConflit() throws IOException {
        git = new GitCommandes(tmp);
        readme = tmp.resolve("README.md");
        git.run("init");
        git.run("config", "user.email", "stagiaire@piscine.fr");
        git.run("config", "user.name", "Stagiaire Piscine Java");

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

        git.tenter("merge", "feature");
    }

    @Test
    void lesDeuxContributionsSontConservees() throws IOException {
        new ResolutionConflit().resoudreConflit(git);
        String contenu = Files.readString(readme, StandardCharsets.UTF_8);
        assertThat(contenu)
            .contains("import")
            .contains("export");
    }

    @Test
    void laFusionEstConclueParUnCommitDeMerge() throws IOException {
        new ResolutionConflit().resoudreConflit(git);
        // Un commit de merge a deux parents (la branche main et la branche feature).
        String parents = git.run("show", "--no-patch", "--format=%P", "HEAD").strip();
        assertThat(parents.split("\\s+")).hasSize(2);
    }
}
