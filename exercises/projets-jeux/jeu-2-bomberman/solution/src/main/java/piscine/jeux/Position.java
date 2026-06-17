package piscine.jeux;

/** FOURNI — ne pas modifier. Une case du plateau (ligne, colonne), 0-indexée. */
public record Position(int ligne, int colonne) {

    /** La case obtenue en se déplaçant d'une case dans la direction donnée. */
    public Position plus(Direction d) {
        return new Position(ligne + d.dLigne(), colonne + d.dColonne());
    }
}
