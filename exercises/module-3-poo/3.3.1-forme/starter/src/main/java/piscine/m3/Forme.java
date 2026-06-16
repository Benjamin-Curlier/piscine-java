package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.3.1 — Forme : classe abstraite (FOURNIE, ne pas modifier).
 *
 * <p>Vos sous-classes Cercle et Rectangle doivent implementer aire() et
 * perimetre(). La methode decrire() est deja ecrite et s'appuie sur elles.</p>
 */
public abstract class Forme {

    public abstract double aire();

    public abstract double perimetre();

    public String decrire() {
        return String.format(Locale.ROOT, "aire = %.2f, perimetre = %.2f", aire(), perimetre());
    }
}
