package etnc.m3;

/**
 * Exercice 3.3.2 — Vehicule (solution de reference) : classe de base concrete.
 */
public class Vehicule {

    protected String marque;
    protected int vitesseMax;

    public Vehicule(String marque, int vitesseMax) {
        this.marque = marque;
        this.vitesseMax = vitesseMax;
    }

    public String getMarque() {
        return marque;
    }

    public int getVitesseMax() {
        return vitesseMax;
    }

    public String decrire() {
        return "Vehicule " + marque + " (" + vitesseMax + " km/h)";
    }
}
