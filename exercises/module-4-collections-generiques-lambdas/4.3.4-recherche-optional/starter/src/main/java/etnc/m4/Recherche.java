package etnc.m4;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Exercice 4.3.4 — Recherche avec Optional.
 *
 * <p>Completez les trois methodes sans appeler Optional.get().</p>
 */
public class Recherche {

    /**
     * Renvoie le premier soldat satisfaisant le critere, ou Optional.empty() si aucun.
     *
     * @param soldats  liste de soldats (non nulle)
     * @param critere  predicat de selection
     * @return Optional contenant le premier soldat match, ou vide
     */
    public static Optional<Soldat> premier(List<Soldat> soldats, Predicate<Soldat> critere) {
        return Optional.empty(); // TODO
    }

    /**
     * Renvoie le soldat de grade le plus eleve, ou Optional.empty() si la liste est vide.
     *
     * @param soldats  liste de soldats (non nulle)
     * @return Optional contenant le soldat de grade maximum, ou vide
     */
    public static Optional<Soldat> plusHautGrade(List<Soldat> soldats) {
        return Optional.empty(); // TODO
    }

    /**
     * Renvoie le nom du premier soldat satisfaisant le critere, ou "Aucun" si aucun.
     *
     * @param soldats  liste de soldats (non nulle)
     * @param critere  predicat de selection
     * @return le nom du soldat trouve, ou "Aucun"
     */
    public static String nomOuParDefaut(List<Soldat> soldats, Predicate<Soldat> critere) {
        return "Aucun"; // TODO
    }
}
