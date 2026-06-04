package etnc.m4;

import java.util.ArrayList;
import java.util.List;

/** Exercice 4.2.1 — Pile générique (LIFO) (solution de reference). */
public class Pile<T> {

    private final List<T> elements = new ArrayList<>();

    public void empiler(T element) {
        elements.add(element);
    }

    public T depiler() {
        if (estVide()) {
            return null;
        }
        return elements.remove(elements.size() - 1);
    }

    public T sommet() {
        if (estVide()) {
            return null;
        }
        return elements.get(elements.size() - 1);
    }

    public boolean estVide() {
        return elements.isEmpty();
    }

    public int taille() {
        return elements.size();
    }
}
