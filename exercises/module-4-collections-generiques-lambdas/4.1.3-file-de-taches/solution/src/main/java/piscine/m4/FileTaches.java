package piscine.m4;

import java.util.ArrayDeque;
import java.util.Deque;

/** Exercice 4.1.3 — FileTaches (solution de reference) : file de taches FIFO. */
public class FileTaches {

    private final Deque<String> file = new ArrayDeque<>();

    public void ajouter(String tache) {
        file.addLast(tache);
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
