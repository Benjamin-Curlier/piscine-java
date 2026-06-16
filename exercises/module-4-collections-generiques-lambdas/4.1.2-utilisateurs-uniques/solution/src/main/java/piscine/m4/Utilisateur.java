package piscine.m4;

import java.util.Objects;

/** Exercice 4.1.2 — Utilisateur (solution de reference) : egalite logique nom + matricule. */
public class Utilisateur {

    private final String nom;
    private final int matricule;

    public Utilisateur(String nom, int matricule) {
        this.nom = nom;
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public int getMatricule() {
        return matricule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur u)) {
            return false;
        }
        return matricule == u.matricule && Objects.equals(nom, u.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, matricule);
    }
}
