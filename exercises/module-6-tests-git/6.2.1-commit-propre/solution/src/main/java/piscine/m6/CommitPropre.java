package piscine.m6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/** Solution de référence : deux commits atomiques, un fichier chacun. */
public class CommitPropre {

    public void creerHistorique(GitCommandes git) throws IOException {
        Files.writeString(git.repo().resolve("notes.txt"), "première note", StandardCharsets.UTF_8);
        git.run("add", "notes.txt");
        git.run("commit", "-m", "Ajoute les notes");

        Files.writeString(git.repo().resolve("liste.txt"), "première tâche", StandardCharsets.UTF_8);
        git.run("add", "liste.txt");
        git.run("commit", "-m", "Ajoute la liste");
    }
}
