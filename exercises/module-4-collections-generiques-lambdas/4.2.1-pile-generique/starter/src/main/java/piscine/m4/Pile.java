package piscine.m4;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercice 4.2.1 — Pile générique (LIFO).
 *
 * <p>L'en-tete generique {@code Pile<T>} est fourni : complétez les méthodes
 * en propageant le type {@code T} (pas d'Object, pas de cast).</p>
 */
public class Pile<T> {

    private final List<T> elements = new ArrayList<>();

    public void empiler(T element) {
        // TODO : ajouter l'element au sommet (elements.add(element))
    }

    public T depiler() {
        // TODO : pile vide -> null ; sinon retirer et renvoyer le dernier (LIFO)
        return null;
    }

    public T sommet() {
        // TODO : pile vide -> null ; sinon renvoyer le dernier SANS le retirer
        return null;
    }

    public boolean estVide() {
        // TODO : true si la pile est vide
        return true;
    }

    public int taille() {
        // TODO : nombre d'elements
        return 0;
    }
}
