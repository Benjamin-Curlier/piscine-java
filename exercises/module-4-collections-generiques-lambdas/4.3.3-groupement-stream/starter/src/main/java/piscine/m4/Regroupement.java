package piscine.m4;

import java.util.List;
import java.util.Map;

/**
 * Exercice 4.3.3 — Regroupement par collecteurs.
 *
 * <p>Completez sans changer les signatures.</p>
 */
public class Regroupement {

    /** Regroupe les membres par niveau. */
    public static Map<Niveau, List<Membre>> parNiveau(List<Membre> membres) {
        return null; // TODO
    }

    /**
     * Partitionne les membres selon que leur anciennete est >= seuil (true) ou non (false).
     * Les deux cles true ET false sont toujours presentes dans le resultat.
     */
    public static Map<Boolean, List<Membre>> selonAnciennete(List<Membre> membres, int seuil) {
        return Map.of(); // TODO
    }

    /** Renvoie le nombre de membres par niveau. */
    public static Map<Niveau, Long> effectifsParNiveau(List<Membre> membres) {
        return null; // TODO
    }
}
