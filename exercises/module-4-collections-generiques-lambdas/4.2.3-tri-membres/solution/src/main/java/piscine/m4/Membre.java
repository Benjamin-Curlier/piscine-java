package piscine.m4;

/** Exercice 4.2.3 — Membre (solution de reference) : ordre naturel par niveau puis nom. */
public record Membre(String nom, Niveau niveau, int anciennete)
        implements Comparable<Membre> {

    @Override
    public int compareTo(Membre autre) {
        int c = this.niveau().compareTo(autre.niveau());
        return c != 0 ? c : this.nom().compareTo(autre.nom());
    }
}
