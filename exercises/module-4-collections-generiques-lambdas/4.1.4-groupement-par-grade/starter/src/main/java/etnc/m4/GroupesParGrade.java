package etnc.m4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Exercice 4.1.4 — Groupes de personnel par grade.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class GroupesParGrade {

    private final Map<Grade, List<String>> parGrade = new HashMap<>();

    /**
     * Affecte un membre au groupe de son grade.
     *
     * @param grade le grade du membre
     * @param nom   le nom du membre
     */
    public void affecter(Grade grade, String nom) {
        // TODO : utiliser computeIfAbsent pour creer la liste si absent, puis add(nom)
    }

    /**
     * Renvoie la liste des membres du grade, ou une liste vide si jamais affecte.
     *
     * @param grade le grade recherche
     * @return liste des membres (jamais null)
     */
    public List<String> membres(Grade grade) {
        return List.of(); // TODO
    }

    /**
     * Renvoie l'ensemble des grades ayant au moins un membre.
     *
     * @return ensemble des grades enregistres
     */
    public Set<Grade> grades() {
        return new java.util.HashSet<>(); // TODO
    }

    /**
     * Renvoie le nombre de membres du grade.
     *
     * @param grade le grade recherche
     * @return nombre de membres (0 si grade jamais affecte)
     */
    public int effectif(Grade grade) {
        return 0; // TODO
    }
}
