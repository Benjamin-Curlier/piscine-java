package piscine.jeux;

/** FOURNI — ne pas modifier. Les quatre directions (delta ligne/colonne, 0-indexé). */
public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1);

    private final int dLigne;
    private final int dColonne;

    Direction(int dLigne, int dColonne) {
        this.dLigne = dLigne;
        this.dColonne = dColonne;
    }

    public int dLigne() {
        return dLigne;
    }

    public int dColonne() {
        return dColonne;
    }
}
