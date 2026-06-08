package etnc.m6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Solution de référence : on retire les marqueurs (en gardant les deux contributions) et on conclut la fusion. */
public class ResolutionConflit {

    public void resoudreConflit(GitCommandes git) throws IOException {
        Path readme = git.repo().resolve("README.md");
        List<String> resolu = new ArrayList<>();
        for (String ligne : Files.readAllLines(readme, StandardCharsets.UTF_8)) {
            if (ligne.startsWith("<<<<<<<") || ligne.startsWith("=======") || ligne.startsWith(">>>>>>>")) {
                continue; // on retire les marqueurs de conflit, on garde le reste
            }
            resolu.add(ligne);
        }
        Files.write(readme, resolu, StandardCharsets.UTF_8);
        git.run("add", "README.md");
        git.run("commit", "-m", "Résout le conflit de fusion");
    }
}
