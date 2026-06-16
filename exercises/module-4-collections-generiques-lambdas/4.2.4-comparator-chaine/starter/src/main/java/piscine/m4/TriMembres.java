package piscine.m4;

import java.util.List;

/**
 * Exercice 4.2.4 — TriMembres : tris par chaînage de Comparator.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class TriMembres {

    private TriMembres() { }

    /**
     * Renvoie une nouvelle liste triée par nom (ordre alphabétique).
     * La liste source n'est pas modifiée.
     */
    public static List<Membre> parNom(List<Membre> membres) {
        return null; // TODO : Comparator.comparing(Membre::nom)
    }

    /**
     * Renvoie une nouvelle liste triée par niveau croissant,
     * puis par nom en cas d'égalité de niveau.
     * La liste source n'est pas modifiée.
     */
    public static List<Membre> parNiveauPuisNom(List<Membre> membres) {
        return null; // TODO : comparing(Membre::niveau).thenComparing(Membre::nom)
    }

    /**
     * Renvoie une nouvelle liste triée par niveau décroissant
     * (PRINCIPAL en premier, JUNIOR en dernier).
     * La liste source n'est pas modifiée.
     */
    public static List<Membre> parNiveauDecroissant(List<Membre> membres) {
        return null; // TODO : comparing(Membre::niveau).reversed()
    }
}
