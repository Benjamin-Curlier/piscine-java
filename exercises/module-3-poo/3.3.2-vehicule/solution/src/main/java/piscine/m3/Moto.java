package piscine.m3;

/** Exercice 3.3.2 — Moto (solution de reference). */
public class Moto extends Vehicule {

    public Moto(String marque, int vitesseMax) {
        super(marque, vitesseMax);
    }

    @Override
    public String decrire() {
        return "Moto " + marque + " (" + vitesseMax + " km/h)";
    }
}
