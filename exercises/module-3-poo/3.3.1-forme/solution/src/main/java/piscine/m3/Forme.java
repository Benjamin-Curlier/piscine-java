package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.3.1 — Forme (solution de reference) : classe abstraite.
 *
 * <p>Methodes abstraites {@link #aire()} et {@link #perimetre()} (chaque forme
 * les calcule), methode concrete {@link #decrire()} qui les appelle (patron de
 * methode : le comportement depend du type reel — polymorphisme).</p>
 */
public abstract class Forme {

    public abstract double aire();

    public abstract double perimetre();

    public String decrire() {
        return String.format(Locale.ROOT, "aire = %.2f, perimetre = %.2f", aire(), perimetre());
    }
}
