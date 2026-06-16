package piscine.m4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Exercice 4.1.4 — Groupes de membres par niveau.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class GroupesParNiveau {

    private final Map<Niveau, List<String>> parNiveau = new HashMap<>();

    /**
     * Affecte un membre au groupe de son niveau.
     *
     * @param niveau le niveau du membre
     * @param nom    le nom du membre
     */
    public void affecter(Niveau niveau, String nom) {
        // TODO : utiliser computeIfAbsent pour creer la liste si absent, puis add(nom)
    }

    /**
     * Renvoie la liste des membres du niveau, ou une liste vide si jamais affecte.
     *
     * @param niveau le niveau recherche
     * @return liste des membres (jamais null)
     */
    public List<String> membres(Niveau niveau) {
        return List.of(); // TODO
    }

    /**
     * Renvoie l'ensemble des niveaux ayant au moins un membre.
     *
     * @return ensemble des niveaux enregistres
     */
    public Set<Niveau> niveaux() {
        return new java.util.HashSet<>(); // TODO
    }

    /**
     * Renvoie le nombre de membres du niveau.
     *
     * @param niveau le niveau recherche
     * @return nombre de membres (0 si niveau jamais affecte)
     */
    public int effectif(Niveau niveau) {
        return 0; // TODO
    }
}
