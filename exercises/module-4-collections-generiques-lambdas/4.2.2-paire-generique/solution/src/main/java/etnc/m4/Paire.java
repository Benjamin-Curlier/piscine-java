package etnc.m4;

/**
 * Exercice 4.2.2 — Paire generique immuable (solution de reference).
 *
 * @param <A> type du premier composant
 * @param <B> type du second composant
 */
public final class Paire<A, B> {

    private final A premier;
    private final B second;

    public Paire(A premier, B second) {
        this.premier = premier;
        this.second = second;
    }

    public A premier() {
        return premier;
    }

    public B second() {
        return second;
    }

    public Paire<B, A> inverser() {
        return new Paire<>(second, premier);
    }

    public static <X, Y> Paire<X, Y> de(X x, Y y) {
        return new Paire<>(x, y);
    }
}
