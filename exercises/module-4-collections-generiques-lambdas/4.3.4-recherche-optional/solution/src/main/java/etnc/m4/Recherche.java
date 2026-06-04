package etnc.m4;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Exercice 4.3.4 — Recherche avec Optional (solution de reference).
 */
public class Recherche {

    /**
     * Renvoie le premier soldat satisfaisant le critere, ou Optional.empty() si aucun.
     */
    public static Optional<Soldat> premier(List<Soldat> soldats, Predicate<Soldat> critere) {
        return soldats.stream().filter(critere).findFirst();
    }

    /**
     * Renvoie le soldat de grade le plus eleve, ou Optional.empty() si la liste est vide.
     */
    public static Optional<Soldat> plusHautGrade(List<Soldat> soldats) {
        return soldats.stream().max(Comparator.comparing(Soldat::grade));
    }

    /**
     * Renvoie le nom du premier soldat satisfaisant le critere, ou "Aucun" si aucun.
     */
    public static String nomOuParDefaut(List<Soldat> soldats, Predicate<Soldat> critere) {
        return premier(soldats, critere).map(Soldat::nom).orElse("Aucun");
    }
}
