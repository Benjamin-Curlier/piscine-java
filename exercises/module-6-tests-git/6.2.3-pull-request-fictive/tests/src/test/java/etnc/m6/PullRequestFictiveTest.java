package etnc.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests publics : la branche est fusionnée dans main et le fichier de PR existe. */
class PullRequestFictiveTest {

    @TempDir
    Path tmp;
    private GitCommandes git;

    @BeforeEach
    void initDepot() throws IOException {
        git = new GitCommandes(tmp);
        git.run("init");
        git.run("config", "user.email", "stagiaire@etnc.fr");
        git.run("config", "user.name", "Stagiaire ETNC");
        Files.writeString(tmp.resolve("README.md"), "# Projet\n", StandardCharsets.UTF_8);
        git.run("add", "README.md");
        git.run("commit", "-m", "Initial");
        git.run("branch", "-M", "main");
    }

    @Test
    void revientSurMainAvecLaLicenceFusionnee() throws IOException {
        new PullRequestFictive().preparerPullRequest(git);
        assertThat(git.brancheCourante()).isEqualTo("main");
        assertThat(Files.exists(tmp.resolve("LICENSE"))).isTrue();
    }

    @Test
    void leFichierPullRequestExiste() throws IOException {
        new PullRequestFictive().preparerPullRequest(git);
        assertThat(Files.exists(tmp.resolve("PULL_REQUEST.md"))).isTrue();
    }
}
