package piscine.moulinette.console.workspace;

import java.util.List;

/**
 * Sous-groupe d'exercices, ordonnés par {@link ExerciseEntry#position()} croissante.
 * Le titre est dérivé du référentiel ou laissé vide si introuvable.
 */
public record SousGroupe(String id, String titre, List<ExerciseEntry> exercices) {
    public SousGroupe {
        if (exercices.size() > 6) {
            throw new IllegalArgumentException(
                "Un sous-groupe ne peut pas contenir plus de 6 exercices (id=" + id + ")");
        }
    }
}
