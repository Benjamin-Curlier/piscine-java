package piscine.m3;

/**
 * Exercice 3.4.3 — Dossier : implemente le contrat Ordonnable.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Dossier implements Ordonnable {

    // TODO : declarer les champs prives titre (String) et priorite (int)

    public Dossier(String titre, int priorite) {
        // TODO : memoriser titre et priorite
    }

    public String getTitre() {
        return null; // TODO
    }

    public int getPriorite() {
        return 0; // TODO
    }

    @Override
    public int comparerA(Ordonnable autre) {
        // TODO : comparer les priorites (indice : (Dossier) autre puis Integer.compare)
        return 0;
    }
}
