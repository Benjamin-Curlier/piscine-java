package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.3.3 — Personnel : hierarchie abstraite (FOURNIE, ne pas modifier).
 *
 * <p>Vos sous-classes doivent implementer niveau() et solde(). La methode
 * fiche() est deja ecrite et s'appuie sur elles.</p>
 */
public abstract class Personnel {

    protected String nom;

    public Personnel(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public abstract String niveau();

    public abstract double solde();

    public String fiche() {
        return String.format(Locale.ROOT, "%s — %s — %.2f €", getNom(), niveau(), solde());
    }
}
