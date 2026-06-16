package piscine.m4;

/**
 * Exercice 4.2.2 — Paire generique immuable a deux parametres de type.
 *
 * <p>Completez sans changer l'en-tete generique ni les signatures publiques.
 * Aucun Object, aucun cast : la genericite passe par les types {@code A}/{@code B}.</p>
 *
 * @param <A> type du premier composant
 * @param <B> type du second composant
 */
public final class Paire<A, B> {

    // TODO : declarer les champs prives final premier (A) et second (B)

    public Paire(A premier, B second) {
        // TODO : memoriser premier et second
    }

    public A premier() {
        return null; // TODO
    }

    public B second() {
        return null; // TODO
    }

    /**
     * Renvoie une nouvelle paire aux composants echanges.
     *
     * @return une {@code Paire<B, A>} dont le premier est le second d'origine
     */
    public Paire<B, A> inverser() {
        return null; // TODO : new Paire<>(second, premier)
    }

    /**
     * Fabrique generique : construit une paire en inferant les types.
     *
     * @param x  premier composant
     * @param y  second composant
     * @param <X> type du premier composant
     * @param <Y> type du second composant
     * @return une {@code Paire<X, Y>}
     */
    public static <X, Y> Paire<X, Y> de(X x, Y y) {
        return null; // TODO : new Paire<>(x, y)
    }
}
