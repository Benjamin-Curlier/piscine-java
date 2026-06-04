package etnc.m4;

import java.util.HashSet;
import java.util.Set;

/**
 * Exercice 4.1.2 — Registre d'utilisateurs sans doublon (s'appuie sur equals/hashCode).
 *
 * <p>Completez les methodes ci-dessous sans changer les signatures.</p>
 */
public class RegistreUtilisateurs {

    private final Set<Utilisateur> utilisateurs = new HashSet<>();

    public boolean ajouter(Utilisateur u) {
        return false; // TODO : ajouter dans l'ensemble (renvoie false si doublon)
    }

    public boolean contient(Utilisateur u) {
        return false; // TODO : l'ensemble contient-il un utilisateur egal ?
    }

    public int nombreDistincts() {
        return 0; // TODO : nombre d'utilisateurs distincts
    }

    public Set<Utilisateur> tous() {
        return new HashSet<>(); // TODO : renvoyer une copie de l'ensemble
    }
}
