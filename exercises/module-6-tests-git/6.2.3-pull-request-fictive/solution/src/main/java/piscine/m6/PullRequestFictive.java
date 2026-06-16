package piscine.m6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/** Solution de référence : cycle PR local (branche → commit → merge dans main) + PULL_REQUEST.md. */
public class PullRequestFictive {

    public void preparerPullRequest(GitCommandes git) throws IOException {
        git.run("switch", "-c", "feature/ajoute-licence");
        Files.writeString(git.repo().resolve("LICENSE"),
            "Licence MIT — Piscine Java\n", StandardCharsets.UTF_8);
        git.run("add", "LICENSE");
        git.run("commit", "-m", "Ajoute le fichier LICENSE");

        git.run("switch", "main");
        git.run("merge", "feature/ajoute-licence");

        Files.writeString(git.repo().resolve("PULL_REQUEST.md"), """
            # Ajoute le fichier LICENSE

            ## Description

            Ajoute un fichier `LICENSE` à la racine du dépôt pour clarifier les droits d'usage.
            - Pourquoi : cadrer la diffusion du projet.
            - Ce qui change : un seul fichier ajouté, aucun code touché.

            ## Checklist de revue

            - [ ] Le fichier LICENSE est présent et lisible
            - [ ] Le titre de la PR décrit le changement
            - [ ] Aucun code de production n'est impacté
            """, StandardCharsets.UTF_8);
    }
}
