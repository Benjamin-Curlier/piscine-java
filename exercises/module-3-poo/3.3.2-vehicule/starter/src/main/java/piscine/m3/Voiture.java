package piscine.m3;

/**
 * Exercice 3.3.2 — Voiture : un vehicule avec un nombre de portes.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Voiture extends Vehicule {

    // TODO : declarer le champ prive nbPortes (int)

    public Voiture(String marque, int vitesseMax, int nbPortes) {
        super(marque, vitesseMax);
        // TODO : memoriser nbPortes
    }

    public int getNbPortes() {
        return 0; // TODO
    }

    @Override
    public String decrire() {
        return null; // TODO : "Voiture <marque> (<vitesseMax> km/h, <nbPortes> portes)"
    }
}
