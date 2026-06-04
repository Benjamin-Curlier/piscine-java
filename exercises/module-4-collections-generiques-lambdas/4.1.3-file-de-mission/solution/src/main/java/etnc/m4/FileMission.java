package etnc.m4;

import java.util.ArrayDeque;
import java.util.Deque;

/** Exercice 4.1.3 — FileMission (solution de reference) : file de missions FIFO. */
public class FileMission {

    private final Deque<String> file = new ArrayDeque<>();

    public void ajouter(String mission) {
        file.addLast(mission);
    }

    public String traiterProchaine() {
        return file.pollFirst();
    }

    public String prochaine() {
        return file.peekFirst();
    }

    public boolean estVide() {
        return file.isEmpty();
    }

    public int taille() {
        return file.size();
    }
}
