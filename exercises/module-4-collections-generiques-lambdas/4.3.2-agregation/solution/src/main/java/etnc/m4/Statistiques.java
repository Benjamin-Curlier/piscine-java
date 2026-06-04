package etnc.m4;

import java.util.List;

/** Exercice 4.3.2 — Reductions numeriques (solution de reference). */
public class Statistiques {

    /** Renvoie le nombre de soldats dans la liste. */
    public static int total(List<Soldat> soldats) {
        return soldats.size();
    }

    /** Renvoie l'anciennete moyenne ; 0.0 si la liste est vide. */
    public static double ancienneteMoyenne(List<Soldat> soldats) {
        return soldats.stream()
                .mapToInt(Soldat::anciennete)
                .average()
                .orElse(0.0);
    }

    /** Renvoie l'anciennete maximale ; 0 si la liste est vide. */
    public static int ancienneteMax(List<Soldat> soldats) {
        return soldats.stream()
                .mapToInt(Soldat::anciennete)
                .max()
                .orElse(0);
    }
}
