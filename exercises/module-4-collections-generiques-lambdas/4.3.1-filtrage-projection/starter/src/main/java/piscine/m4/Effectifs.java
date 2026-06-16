package piscine.m4;

import java.util.List;
import java.util.function.Predicate;

/**
 * Exercice 4.3.1 — Filtrage et projection.
 *
 * <p>Completez les deux methodes en utilisant un pipeline stream idiomatique.</p>
 */
public class Effectifs {

    /**
     * Renvoie les noms des membres dont le niveau est superieur ou egal a {@code min},
     * dans l'ordre source.
     *
     * @param membres liste source (non nulle)
     * @param min     niveau minimum (inclus)
     * @return liste des noms correspondants (vide si aucun ne satisfait)
     */
    public static List<String> nomsDesNiveauxAuMoins(List<Membre> membres, Niveau min) {
        return null; // TODO
    }

    /**
     * Renvoie la sous-liste des membres satisfaisant le critere.
     *
     * @param membres  liste source (non nulle)
     * @param critere  predicate applique a chaque membre
     * @return liste des membres pour lesquels {@code critere} est vrai (vide si aucun)
     */
    public static List<Membre> filtrer(List<Membre> membres, Predicate<Membre> critere) {
        return null; // TODO
    }
}
