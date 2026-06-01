package etnc.piscine.moulinette.framework;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Contexte fourni à chaque {@link Checker} lors de l'évaluation d'un rendu.
 *
 * @param exerciseId      identifiant canonique de l'exercice (ex. {@code "1.1.1"})
 * @param renduPath       répertoire du rendu stagiaire (contient {@code starter/})
 * @param exerciseRefPath répertoire de référence de l'exercice dans le repo Piscine
 *                        (contient {@code tests/}, {@code tests-prives/}, {@code solution/}, {@code evaluation.yml})
 */
public record CheckerContext(
        String exerciseId,
        Path renduPath,
        Path exerciseRefPath
) {
    public CheckerContext {
        Objects.requireNonNull(exerciseId, "exerciseId ne peut pas être null");
        Objects.requireNonNull(renduPath,  "renduPath ne peut pas être null");
        Objects.requireNonNull(exerciseRefPath, "exerciseRefPath ne peut pas être null");
        if (exerciseId.isBlank()) {
            throw new IllegalArgumentException("exerciseId ne peut pas être vide");
        }
    }
}
