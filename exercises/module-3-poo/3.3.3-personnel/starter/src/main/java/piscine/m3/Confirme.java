package piscine.m3;

/**
 * Exercice 3.3.3 — Confirme (niveau "Confirmé", solde 2200 + 100 * anciennete).
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Confirme extends Personnel {

    // TODO : declarer le champ prive anciennete (int)

    public Confirme(String nom, int anciennete) {
        super(nom);
        // TODO : memoriser anciennete
    }

    @Override
    public String niveau() {
        return null; // TODO
    }

    @Override
    public double solde() {
        return 0; // TODO : 2200 + 100 * anciennete
    }
}
