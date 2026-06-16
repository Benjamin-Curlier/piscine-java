package piscine.m4;

/**
 * Exercice 4.2.3 — Membre : implementer l'ordre naturel via Comparable.
 *
 * <p>Completez compareTo sans changer la signature.
 * Regle : d'abord par niveau croissant, puis par nom alphabetique a niveau egal.</p>
 */
public record Membre(String nom, Niveau niveau, int anciennete)
        implements Comparable<Membre> {

    @Override
    public int compareTo(Membre autre) {
        return 0; // TODO : d'abord par niveau (this.niveau().compareTo(autre.niveau())),
                  //        puis par nom si egalite (this.nom().compareTo(autre.nom()))
    }
}
