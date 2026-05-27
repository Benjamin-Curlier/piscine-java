package etnc.piscine.moulinette.framework;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Contexte fourni à chaque {@link Checker} lors de l'évaluation d'un rendu.
 *
 * @param exerciseId identifiant canonique de l'exercice (ex. {@code "1.1.1"})
 * @param renduPath  chemin absolu vers le répertoire rendu par l'étudiant
 */
public record CheckerContext(
        String exerciseId,
        Path renduPath
) {
    /** Valide les champs à la construction. */
    public CheckerContext {
        Objects.requireNonNull(exerciseId, "exerciseId ne peut pas être null");
        Objects.requireNonNull(renduPath,  "renduPath ne peut pas être null");
        if (exerciseId.isBlank()) {
            throw new IllegalArgumentException("exerciseId ne peut pas être vide");
        }
    }
}
