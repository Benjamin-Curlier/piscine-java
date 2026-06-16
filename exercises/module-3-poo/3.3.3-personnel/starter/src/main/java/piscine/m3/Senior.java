package piscine.m3;

/**
 * Exercice 3.3.3 — Senior (niveau "Senior", solde 3000 + 200 * echelon).
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Senior extends Personnel {

    // TODO : declarer le champ prive echelon (int)

    public Senior(String nom, int echelon) {
        super(nom);
        // TODO : memoriser echelon
    }

    @Override
    public String niveau() {
        return null; // TODO
    }

    @Override
    public double solde() {
        return 0; // TODO : 3000 + 200 * echelon
    }
}
