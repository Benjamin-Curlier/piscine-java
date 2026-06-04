package etnc.m4;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Exercice 4.3.1 — Filtrage et projection (solution de reference).
 */
public class Effectifs {

    /**
     * Renvoie les noms des soldats dont le grade est superieur ou egal a {@code min},
     * dans l'ordre source.
     */
    public static List<String> nomsDesGradesAuMoins(List<Soldat> soldats, Grade min) {
        return soldats.stream()
                .filter(s -> s.grade().compareTo(min) >= 0)
                .map(Soldat::nom)
                .collect(Collectors.toList());
    }

    /**
     * Renvoie la sous-liste des soldats satisfaisant le critere.
     */
    public static List<Soldat> filtrer(List<Soldat> soldats, Predicate<Soldat> critere) {
        return soldats.stream()
                .filter(critere)
                .collect(Collectors.toList());
    }
}
