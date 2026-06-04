package etnc.m4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Exercice 4.2.4 — TriSoldats (solution de reference). */
public class TriSoldats {

    private TriSoldats() { }

    /** Renvoie une nouvelle liste triée par nom (ordre alphabétique). */
    public static List<Soldat> parNom(List<Soldat> soldats) {
        Comparator<Soldat> comparateur = Comparator.comparing(Soldat::nom);
        List<Soldat> copie = new ArrayList<>(soldats);
        copie.sort(comparateur);
        return copie;
    }

    /** Renvoie une nouvelle liste triée par grade croissant, puis par nom. */
    public static List<Soldat> parGradePuisNom(List<Soldat> soldats) {
        Comparator<Soldat> comparateur = Comparator.comparing(Soldat::grade)
                .thenComparing(Soldat::nom);
        List<Soldat> copie = new ArrayList<>(soldats);
        copie.sort(comparateur);
        return copie;
    }

    /** Renvoie une nouvelle liste triée par grade décroissant. */
    public static List<Soldat> parGradeDecroissant(List<Soldat> soldats) {
        Comparator<Soldat> comparateur = Comparator.comparing(Soldat::grade).reversed();
        List<Soldat> copie = new ArrayList<>(soldats);
        copie.sort(comparateur);
        return copie;
    }
}
