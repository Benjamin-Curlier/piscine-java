package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.3.4 — Forme : classe abstraite (FOURNIE).
 */
public abstract class Forme {

    public abstract double aire();

    public abstract double perimetre();

    public String decrire() {
        return String.format(Locale.ROOT, "aire = %.2f, perimetre = %.2f", aire(), perimetre());
    }
}
