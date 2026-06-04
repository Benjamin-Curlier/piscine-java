package etnc.m4;

import java.util.List;
import java.util.Map;

/**
 * Exercice 4.3.3 — Regroupement par collecteurs.
 *
 * <p>Completez sans changer les signatures.</p>
 */
public class Regroupement {

    /** Regroupe les soldats par grade. */
    public static Map<Grade, List<Soldat>> parGrade(List<Soldat> soldats) {
        return null; // TODO
    }

    /**
     * Partitionne les soldats selon que leur anciennete est >= seuil (true) ou non (false).
     * Les deux cles true ET false sont toujours presentes dans le resultat.
     */
    public static Map<Boolean, List<Soldat>> selonAnciennete(List<Soldat> soldats, int seuil) {
        return Map.of(); // TODO
    }

    /** Renvoie le nombre de soldats par grade. */
    public static Map<Grade, Long> effectifsParGrade(List<Soldat> soldats) {
        return null; // TODO
    }
}
