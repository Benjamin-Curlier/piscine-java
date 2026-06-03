package etnc.m3;

/**
 * Exercice 3.3.2 — Moto : un vehicule simple.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Moto extends Vehicule {

    public Moto(String marque, int vitesseMax) {
        super(marque, vitesseMax);
    }

    @Override
    public String decrire() {
        return null; // TODO : "Moto <marque> (<vitesseMax> km/h)"
    }
}
