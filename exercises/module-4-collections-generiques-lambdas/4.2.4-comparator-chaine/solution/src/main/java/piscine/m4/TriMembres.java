package piscine.m4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Exercice 4.2.4 — TriMembres (solution de reference). */
public class TriMembres {

    private TriMembres() { }

    /** Renvoie une nouvelle liste triée par nom (ordre alphabétique). */
    public static List<Membre> parNom(List<Membre> membres) {
        Comparator<Membre> comparateur = Comparator.comparing(Membre::nom);
        List<Membre> copie = new ArrayList<>(membres);
        copie.sort(comparateur);
        return copie;
    }

    /** Renvoie une nouvelle liste triée par niveau croissant, puis par nom. */
    public static List<Membre> parNiveauPuisNom(List<Membre> membres) {
        Comparator<Membre> comparateur = Comparator.comparing(Membre::niveau)
                .thenComparing(Membre::nom);
        List<Membre> copie = new ArrayList<>(membres);
        copie.sort(comparateur);
        return copie;
    }

    /** Renvoie une nouvelle liste triée par niveau décroissant. */
    public static List<Membre> parNiveauDecroissant(List<Membre> membres) {
        Comparator<Membre> comparateur = Comparator.comparing(Membre::niveau).reversed();
        List<Membre> copie = new ArrayList<>(membres);
        copie.sort(comparateur);
        return copie;
    }
}
