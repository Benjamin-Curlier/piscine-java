package piscine.moulinette.console.gamification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import piscine.moulinette.console.workspace.ExerciseCatalog;

/** Construit la liste d'{@link Exo} (id, module, difficulté) à partir du catalogue. */
public final class AdaptateurCatalogue {

    private AdaptateurCatalogue() { }

    public static List<Exo> exos(ExerciseCatalog catalog) {
        List<Exo> exos = new ArrayList<>();
        catalog.sousGroupes().forEach(sg ->
            sg.exercices().forEach(e ->
                exos.add(new Exo(e.id(), e.module(), lireDifficulte(e.exerciseDir())))));
        return exos;
    }

    /** Lit le champ {@code difficulte:} du metadata.yml de l'exercice. FACILE par défaut. */
    private static Difficulte lireDifficulte(Path exoDir) {
        Path meta = exoDir.resolve("metadata.yml");
        try {
            for (String ligne : Files.readAllLines(meta)) {
                String s = ligne.strip();
                if (s.startsWith("difficulte:")) {
                    String valeur = s.substring("difficulte:".length()).strip()
                        .replace("\"", "").replace("'", "");
                    return Difficulte.depuis(valeur);
                }
            }
        } catch (IOException ignore) {
            // difficulté par défaut si le fichier est absent/illisible
        }
        return Difficulte.FACILE;
    }
}
