package piscine.m6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests privés : atomicité (un fichier par commit), messages, arbre de travail propre. */
class CommitProprePriveTest {

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
    void chaqueCommitEstAtomique() throws IOException {
        new CommitPropre().creerHistorique(git);
        String premier = git.run("show", "--name-only", "--format=", "HEAD~1").strip();
        String dernier = git.run("show", "--name-only", "--format=", "HEAD").strip();
        assertThat(premier).isEqualTo("notes.txt");
        assertThat(dernier).isEqualTo("liste.txt");
    }

    @Test
    void messagesDeCommitPresents() throws IOException {
        new CommitPropre().creerHistorique(git);
        assertThat(git.log())
            .contains("Ajoute les notes")
            .contains("Ajoute la liste");
    }

    @Test
    void arbreDeTravailPropreApres() throws IOException {
        new CommitPropre().creerHistorique(git);
        assertThat(git.run("status", "--porcelain").strip()).isEmpty();
    }
}
