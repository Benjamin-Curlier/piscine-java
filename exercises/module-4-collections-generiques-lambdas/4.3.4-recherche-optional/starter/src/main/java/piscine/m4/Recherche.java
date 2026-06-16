package piscine.m4;

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
     * Renvoie le premier membre satisfaisant le critere, ou Optional.empty() si aucun.
     *
     * @param membres  liste de membres (non nulle)
     * @param critere  predicat de selection
     * @return Optional contenant le premier membre match, ou vide
     */
    public static Optional<Membre> premier(List<Membre> membres, Predicate<Membre> critere) {
        return Optional.empty(); // TODO
    }

    /**
     * Renvoie le membre de niveau le plus eleve, ou Optional.empty() si la liste est vide.
     *
     * @param membres  liste de membres (non nulle)
     * @return Optional contenant le membre de niveau maximum, ou vide
     */
    public static Optional<Membre> plusHautNiveau(List<Membre> membres) {
        return Optional.empty(); // TODO
    }

    /**
     * Renvoie le nom du premier membre satisfaisant le critere, ou "Aucun" si aucun.
     *
     * @param membres  liste de membres (non nulle)
     * @param critere  predicat de selection
     * @return le nom du membre trouve, ou "Aucun"
     */
    public static String nomOuParDefaut(List<Membre> membres, Predicate<Membre> critere) {
        return "Aucun"; // TODO
    }
}
