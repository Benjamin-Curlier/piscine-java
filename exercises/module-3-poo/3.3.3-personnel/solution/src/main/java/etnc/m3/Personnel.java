package etnc.m3;

import java.util.Locale;

/**
 * Exercice 3.3.3 — Personnel (solution de reference) : hierarchie abstraite.
 *
 * <p>Deux methodes abstraites (grade, solde) specialisees par chaque categorie,
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

    public abstract String grade();

    public abstract double solde();

    public String fiche() {
        return String.format(Locale.ROOT, "%s — %s — %.2f €", getNom(), grade(), solde());
    }
}
