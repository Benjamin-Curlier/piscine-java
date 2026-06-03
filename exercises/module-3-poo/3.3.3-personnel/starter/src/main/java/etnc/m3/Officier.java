package etnc.m3;

/**
 * Exercice 3.3.3 — Officier (grade "Officier", solde 3000 + 200 * echelon).
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Officier extends Personnel {

    // TODO : declarer le champ prive echelon (int)

    public Officier(String nom, int echelon) {
        super(nom);
        // TODO : memoriser echelon
    }

    @Override
    public String grade() {
        return null; // TODO
    }

    @Override
    public double solde() {
        return 0; // TODO : 3000 + 200 * echelon
    }
}
