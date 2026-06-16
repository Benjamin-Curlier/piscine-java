package piscine.m4;

import java.util.List;

/** Exercice 4.3.2 — Reductions numeriques (solution de reference). */
public class Statistiques {

    /** Renvoie le nombre de membres dans la liste. */
    public static int total(List<Membre> membres) {
        return membres.size();
    }

    /** Renvoie l'anciennete moyenne ; 0.0 si la liste est vide. */
    public static double ancienneteMoyenne(List<Membre> membres) {
        return membres.stream()
                .mapToInt(Membre::anciennete)
                .average()
                .orElse(0.0);
    }

    /** Renvoie l'anciennete maximale ; 0 si la liste est vide. */
    public static int ancienneteMax(List<Membre> membres) {
        return membres.stream()
                .mapToInt(Membre::anciennete)
                .max()
                .orElse(0);
    }
}
