package piscine.m4;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Exercice 4.3.3 — Regroupement par collecteurs (solution de reference). */
public class Regroupement {

    /** Regroupe les membres par niveau. */
    public static Map<Niveau, List<Membre>> parNiveau(List<Membre> membres) {
        return membres.stream().collect(Collectors.groupingBy(Membre::niveau));
    }

    /**
     * Partitionne les membres selon que leur anciennete est >= seuil (true) ou non (false).
     * Les deux cles true ET false sont toujours presentes dans le resultat.
     */
    public static Map<Boolean, List<Membre>> selonAnciennete(List<Membre> membres, int seuil) {
        return membres.stream().collect(Collectors.partitioningBy(s -> s.anciennete() >= seuil));
    }

    /** Renvoie le nombre de membres par niveau. */
    public static Map<Niveau, Long> effectifsParNiveau(List<Membre> membres) {
        return membres.stream().collect(Collectors.groupingBy(Membre::niveau, Collectors.counting()));
    }
}
