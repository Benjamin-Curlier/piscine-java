package piscine.m4;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Exercice 4.3.1 — Filtrage et projection (solution de reference).
 */
public class Effectifs {

    /**
     * Renvoie les noms des membres dont le niveau est superieur ou egal a {@code min},
     * dans l'ordre source.
     */
    public static List<String> nomsDesNiveauxAuMoins(List<Membre> membres, Niveau min) {
        return membres.stream()
                .filter(s -> s.niveau().compareTo(min) >= 0)
                .map(Membre::nom)
                .collect(Collectors.toList());
    }

    /**
     * Renvoie la sous-liste des membres satisfaisant le critere.
     */
    public static List<Membre> filtrer(List<Membre> membres, Predicate<Membre> critere) {
        return membres.stream()
                .filter(critere)
                .collect(Collectors.toList());
    }
}
