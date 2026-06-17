package piscine.m3;

import java.util.Objects;

/**
 * Exercice 3.1.4 — Carte (solution de reference).
 *
 * <p>Carte a jouer immuable, comparee par VALEUR : deux cartes sont egales si
 * elles ont la meme valeur et la meme couleur. equals et hashCode sont
 * redefinis de maniere coherente.</p>
 */
public class Carte {

    private final int valeur;
    private final String couleur;

    public Carte(int valeur, String couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
    }

    public int getValeur() {
        return valeur;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public boolean equals(Object autre) {
        if (this == autre) {
            return true;
        }
        if (autre == null || getClass() != autre.getClass()) {
            return false;
        }
        Carte carte = (Carte) autre;
        return valeur == carte.valeur && Objects.equals(couleur, carte.couleur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur, couleur);
    }

    @Override
    public String toString() {
        return valeur + " de " + couleur;
    }
}
