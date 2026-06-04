package etnc.m4;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Exercice 4.1.3 — FileMission : file de missions FIFO.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class FileMission {

    private final Deque<String> file = new ArrayDeque<>();

    public void ajouter(String mission) {
        // TODO : ajouter la mission en fin de file (addLast)
    }

    public String traiterProchaine() {
        return null; // TODO : retirer et renvoyer la premiere mission (pollFirst) ; null si vide
    }

    public String prochaine() {
        return null; // TODO : renvoyer la premiere mission SANS la retirer (peekFirst) ; null si vide
    }

    public boolean estVide() {
        return false; // TODO
    }

    public int taille() {
        return 0; // TODO
    }
}
