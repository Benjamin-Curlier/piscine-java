package etnc.m4;

import java.util.List;

/**
 * Exercice 4.2.4 — TriSoldats : tris par chaînage de Comparator.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class TriSoldats {

    private TriSoldats() { }

    /**
     * Renvoie une nouvelle liste triée par nom (ordre alphabétique).
     * La liste source n'est pas modifiée.
     */
    public static List<Soldat> parNom(List<Soldat> soldats) {
        return null; // TODO : Comparator.comparing(Soldat::nom)
    }

    /**
     * Renvoie une nouvelle liste triée par grade croissant,
     * puis par nom en cas d'égalité de grade.
     * La liste source n'est pas modifiée.
     */
    public static List<Soldat> parGradePuisNom(List<Soldat> soldats) {
        return null; // TODO : comparing(Soldat::grade).thenComparing(Soldat::nom)
    }

    /**
     * Renvoie une nouvelle liste triée par grade décroissant
     * (LIEUTENANT en premier, SOLDAT en dernier).
     * La liste source n'est pas modifiée.
     */
    public static List<Soldat> parGradeDecroissant(List<Soldat> soldats) {
        return null; // TODO : comparing(Soldat::grade).reversed()
    }
}
