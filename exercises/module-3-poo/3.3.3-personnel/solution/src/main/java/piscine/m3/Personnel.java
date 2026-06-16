package piscine.m3;

import java.util.Locale;

/**
 * Exercice 3.3.3 — Personnel (solution de reference) : hierarchie abstraite.
 *
 * <p>Deux methodes abstraites (niveau, solde) specialisees par chaque categorie,
 * une methode concrete (fiche) qui les combine (polymorphisme).</p>
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
