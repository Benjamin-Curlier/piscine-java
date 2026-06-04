package etnc.m4;

import java.util.List;
import java.util.function.Predicate;

/**
 * Exercice 4.3.1 — Filtrage et projection.
 *
 * <p>Completez les deux methodes en utilisant un pipeline stream idiomatique.</p>
 */
public class Effectifs {

    /**
     * Renvoie les noms des soldats dont le grade est superieur ou egal a {@code min},
     * dans l'ordre source.
     *
     * @param soldats liste source (non nulle)
     * @param min     grade minimum (inclus)
     * @return liste des noms correspondants (vide si aucun ne satisfait)
     */
    public static List<String> nomsDesGradesAuMoins(List<Soldat> soldats, Grade min) {
        return null; // TODO
    }

    /**
     * Renvoie la sous-liste des soldats satisfaisant le critere.
     *
     * @param soldats  liste source (non nulle)
     * @param critere  predicate applique a chaque soldat
     * @return liste des soldats pour lesquels {@code critere} est vrai (vide si aucun)
     */
    public static List<Soldat> filtrer(List<Soldat> soldats, Predicate<Soldat> critere) {
        return null; // TODO
    }
}
