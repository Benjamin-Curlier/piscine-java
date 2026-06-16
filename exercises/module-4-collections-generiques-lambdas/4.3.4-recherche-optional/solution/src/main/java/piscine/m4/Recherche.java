package piscine.m4;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Exercice 4.3.4 — Recherche avec Optional (solution de reference).
 */
public class Recherche {

    /**
     * Renvoie le premier membre satisfaisant le critere, ou Optional.empty() si aucun.
     */
    public static Optional<Membre> premier(List<Membre> membres, Predicate<Membre> critere) {
        return membres.stream().filter(critere).findFirst();
    }

    /**
     * Renvoie le membre de niveau le plus eleve, ou Optional.empty() si la liste est vide.
     */
    public static Optional<Membre> plusHautNiveau(List<Membre> membres) {
        return membres.stream().max(Comparator.comparing(Membre::niveau));
    }

    /**
     * Renvoie le nom du premier membre satisfaisant le critere, ou "Aucun" si aucun.
     */
    public static String nomOuParDefaut(List<Membre> membres, Predicate<Membre> critere) {
        return premier(membres, critere).map(Membre::nom).orElse("Aucun");
    }
}
