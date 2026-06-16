package piscine.m4;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Exercice 4.1.3 — FileTaches : file de taches FIFO.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class FileTaches {

    private final Deque<String> file = new ArrayDeque<>();

    public void ajouter(String tache) {
        // TODO : ajouter la tache en fin de file (addLast)
    }

    public String traiterProchaine() {
        return null; // TODO : retirer et renvoyer la premiere tache (pollFirst) ; null si vide
    }

    public String prochaine() {
        return null; // TODO : renvoyer la premiere tache SANS la retirer (peekFirst) ; null si vide
    }

    public boolean estVide() {
        return false; // TODO
    }

    public int taille() {
        return 0; // TODO
    }
}
