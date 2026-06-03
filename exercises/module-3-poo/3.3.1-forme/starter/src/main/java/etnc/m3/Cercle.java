package etnc.m3;

/**
 * Exercice 3.3.1 — Cercle : un cercle defini par son rayon.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Cercle extends Forme {

    // TODO : declarer le champ prive rayon (double)

    public Cercle(double rayon) {
        // TODO : memoriser le rayon
    }

    public double getRayon() {
        return 0; // TODO
    }

    @Override
    public double aire() {
        return 0; // TODO : Math.PI * rayon * rayon
    }

    @Override
    public double perimetre() {
        return 0; // TODO : 2 * Math.PI * rayon
    }
}
