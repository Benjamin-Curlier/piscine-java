package etnc.m4;

/**
 * Exercice 4.2.3 — Soldat : implementer l'ordre naturel via Comparable.
 *
 * <p>Completez compareTo sans changer la signature.
 * Regle : d'abord par grade croissant, puis par nom alphabetique a grade egal.</p>
 */
public record Soldat(String nom, Grade grade, int anciennete)
        implements Comparable<Soldat> {

    @Override
    public int compareTo(Soldat autre) {
        return 0; // TODO : d'abord par grade (this.grade().compareTo(autre.grade())),
                  //        puis par nom si egalite (this.nom().compareTo(autre.nom()))
    }
}
