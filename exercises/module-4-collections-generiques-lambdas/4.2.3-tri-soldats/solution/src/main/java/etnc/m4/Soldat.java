package etnc.m4;

/** Exercice 4.2.3 — Soldat (solution de reference) : ordre naturel par grade puis nom. */
public record Soldat(String nom, Grade grade, int anciennete)
        implements Comparable<Soldat> {

    @Override
    public int compareTo(Soldat autre) {
        int c = this.grade().compareTo(autre.grade());
        return c != 0 ? c : this.nom().compareTo(autre.nom());
    }
}
