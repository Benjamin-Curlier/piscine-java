package etnc.m3;

/** Exercice 3.3.2 — Voiture (solution de reference). */
public class Voiture extends Vehicule {

    private int nbPortes;

    public Voiture(String marque, int vitesseMax, int nbPortes) {
        super(marque, vitesseMax);
        this.nbPortes = nbPortes;
    }

    public int getNbPortes() {
        return nbPortes;
    }

    @Override
    public String decrire() {
        return "Voiture " + marque + " (" + vitesseMax + " km/h, " + nbPortes + " portes)";
    }
}
