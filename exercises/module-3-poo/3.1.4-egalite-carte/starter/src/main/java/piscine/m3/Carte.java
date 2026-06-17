package piscine.m3;

/**
 * Exercice 3.1.4 — Carte : carte a jouer comparee par VALEUR.
 *
 * <p>Deux cartes doivent etre egales lorsqu'elles ont la meme valeur ET la meme
 * couleur. Completez equals et hashCode de maniere coherente, sans changer les
 * signatures publiques.</p>
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
        // TODO : reflexivite (this == autre), rejet de null et d'un type different,
        // puis comparaison de valeur ET couleur (indice : Objects.equals pour la couleur)
        return false;
    }

    @Override
    public int hashCode() {
        // TODO : produire le MEME code pour deux cartes egales
        // (indice : java.util.Objects.hash(valeur, couleur))
        return 0;
    }

    @Override
    public String toString() {
        // TODO : format "valeur de couleur" (ex. "7 de Coeur")
        return "";
    }
}
