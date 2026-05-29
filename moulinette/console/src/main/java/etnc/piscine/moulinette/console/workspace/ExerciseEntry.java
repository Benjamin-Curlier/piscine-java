package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Entrée du catalogue : un exercice avec ses chemins.
 *
 * @param id          id canonique ("1.1.1")
 * @param slug        slug humain ("hello-world")
 * @param module      numéro de module (1..6)
 * @param sousGroupe  id du sous-groupe ("1.1")
 * @param position    rang dans le sous-groupe (1 = plus facile)
 * @param notions     liste des notions touchées
 * @param exerciseDir dossier source dans le repo Piscine (exercises/.../1.1.1-hello-world)
 */
public record ExerciseEntry(
        String id, String slug, int module, String sousGroupe,
        int position, List<String> notions, Path exerciseDir
) {
    public ExerciseEntry {
        Objects.requireNonNull(id); Objects.requireNonNull(sousGroupe);
        Objects.requireNonNull(exerciseDir);
    }
}
