package etnc.m4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Exercice 4.1.4 — Groupes de personnel par grade (solution de reference). */
public class GroupesParGrade {

    private final Map<Grade, List<String>> parGrade = new HashMap<>();

    public void affecter(Grade grade, String nom) {
        parGrade.computeIfAbsent(grade, g -> new ArrayList<>()).add(nom);
    }

    public List<String> membres(Grade grade) {
        return parGrade.getOrDefault(grade, List.of());
    }

    public Set<Grade> grades() {
        return parGrade.keySet();
    }

    public int effectif(Grade grade) {
        return membres(grade).size();
    }
}
