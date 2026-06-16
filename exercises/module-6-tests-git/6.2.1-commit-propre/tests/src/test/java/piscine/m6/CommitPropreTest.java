package piscine.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests publics : on monte un dépôt-jouet, on appelle la méthode du stagiaire, on inspecte l'état. */
class CommitPropreTest {

    @TempDir
    Path tmp;
    private GitCommandes git;

    @BeforeEach
    void initDepot() {
        git = new GitCommandes(tmp);
        git.run("init");
        git.run("config", "user.email", "stagiaire@piscine.fr");
        git.run("config", "user.name", "Stagiaire Piscine Java");
    }

    @Test
    void produitDeuxCommits() throws IOException {
        new CommitPropre().creerHistorique(git);
        long nbCommits = git.log().lines().filter(l -> !l.isBlank()).count();
        assertThat(nbCommits).isEqualTo(2);
    }

    @Test
    void lesDeuxFichiersExistent() throws IOException {
        new CommitPropre().creerHistorique(git);
        assertThat(Files.exists(tmp.resolve("notes.txt"))).isTrue();
        assertThat(Files.exists(tmp.resolve("liste.txt"))).isTrue();
    }
}
