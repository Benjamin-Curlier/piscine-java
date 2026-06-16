package piscine.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests privés : main contient bien le commit de la branche, et la PR est structurée. */
class PullRequestFictivePriveTest {

    @TempDir
    Path tmp;
    private GitCommandes git;

    @BeforeEach
    void initDepot() throws IOException {
        git = new GitCommandes(tmp);
        git.run("init");
        git.run("config", "user.email", "stagiaire@piscine.fr");
        git.run("config", "user.name", "Stagiaire Piscine Java");
        Files.writeString(tmp.resolve("README.md"), "# Projet\n", StandardCharsets.UTF_8);
        git.run("add", "README.md");
        git.run("commit", "-m", "Initial");
        git.run("branch", "-M", "main");
    }

    @Test
    void mainContientLeCommitDeLaBranche() throws IOException {
        new PullRequestFictive().preparerPullRequest(git);
        assertThat(git.log()).contains("Ajoute le fichier LICENSE");
    }

    @Test
    void lePullRequestEstStructure() throws IOException {
        new PullRequestFictive().preparerPullRequest(git);
        String pr = Files.readString(tmp.resolve("PULL_REQUEST.md"), StandardCharsets.UTF_8);
        assertThat(pr)
            .contains("## Description")
            .contains("## Checklist");
    }
}
