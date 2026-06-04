package etnc.m4;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Exercice 4.3.3 — Regroupement par collecteurs (solution de reference). */
public class Regroupement {

    /** Regroupe les soldats par grade. */
    public static Map<Grade, List<Soldat>> parGrade(List<Soldat> soldats) {
        return soldats.stream().collect(Collectors.groupingBy(Soldat::grade));
    }

    /**
     * Partitionne les soldats selon que leur anciennete est >= seuil (true) ou non (false).
     * Les deux cles true ET false sont toujours presentes dans le resultat.
     */
    public static Map<Boolean, List<Soldat>> selonAnciennete(List<Soldat> soldats, int seuil) {
        return soldats.stream().collect(Collectors.partitioningBy(s -> s.anciennete() >= seuil));
    }

    /** Renvoie le nombre de soldats par grade. */
    public static Map<Grade, Long> effectifsParGrade(List<Soldat> soldats) {
        return soldats.stream().collect(Collectors.groupingBy(Soldat::grade, Collectors.counting()));
    }
}
