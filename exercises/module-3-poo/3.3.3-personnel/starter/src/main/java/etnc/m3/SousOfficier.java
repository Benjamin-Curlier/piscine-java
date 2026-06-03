package etnc.m3;

/**
 * Exercice 3.3.3 — SousOfficier (grade "Sous-officier", solde 2200 + 100 * anciennete).
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class SousOfficier extends Personnel {

    // TODO : declarer le champ prive anciennete (int)

    public SousOfficier(String nom, int anciennete) {
        super(nom);
        // TODO : memoriser anciennete
    }

    @Override
    public String grade() {
        return null; // TODO
    }

    @Override
    public double solde() {
        return 0; // TODO : 2200 + 100 * anciennete
    }
}
