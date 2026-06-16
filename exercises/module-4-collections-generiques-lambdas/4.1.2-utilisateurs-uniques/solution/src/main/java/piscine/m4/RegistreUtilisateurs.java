package piscine.m4;

import java.util.HashSet;
import java.util.Set;

/** Exercice 4.1.2 — Registre d'utilisateurs sans doublon (solution de reference). */
public class RegistreUtilisateurs {

    private final Set<Utilisateur> utilisateurs = new HashSet<>();

    public boolean ajouter(Utilisateur u) {
        return utilisateurs.add(u);
    }

    public boolean contient(Utilisateur u) {
        return utilisateurs.contains(u);
    }

    public int nombreDistincts() {
        return utilisateurs.size();
    }

    public Set<Utilisateur> tous() {
        return new HashSet<>(utilisateurs);
    }
}
