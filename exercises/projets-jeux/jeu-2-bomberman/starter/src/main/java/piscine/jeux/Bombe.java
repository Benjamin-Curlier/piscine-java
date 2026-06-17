package piscine.jeux;

/**
 * FOURNI — ne pas modifier. Une bombe posée.
 *
 * @param position où elle est posée
 * @param compteur nombre de {@code tick()} avant l'explosion (explose quand il atteint 0)
 * @param portee   nombre de cases atteintes par le souffle dans chaque direction
 */
public record Bombe(Position position, int compteur, int portee) { }
