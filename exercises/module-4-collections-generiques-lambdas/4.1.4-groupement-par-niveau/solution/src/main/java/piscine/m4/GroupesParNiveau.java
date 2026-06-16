package piscine.m4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Exercice 4.1.4 — Groupes de membres par niveau (solution de reference). */
public class GroupesParNiveau {

    private final Map<Niveau, List<String>> parNiveau = new HashMap<>();

    public void affecter(Niveau niveau, String nom) {
        parNiveau.computeIfAbsent(niveau, n -> new ArrayList<>()).add(nom);
    }

    public List<String> membres(Niveau niveau) {
        return parNiveau.getOrDefault(niveau, List.of());
    }

    public Set<Niveau> niveaux() {
        return parNiveau.keySet();
    }

    public int effectif(Niveau niveau) {
        return membres(niveau).size();
    }
}
