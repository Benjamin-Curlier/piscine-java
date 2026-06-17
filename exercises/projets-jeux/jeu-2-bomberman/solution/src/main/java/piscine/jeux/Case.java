package piscine.jeux;

/** FOURNI — ne pas modifier. Contenu d'une case du plateau. */
public enum Case {
    VIDE,
    MUR,     // indestructible : bloque le joueur et le souffle
    CAISSE;  // destructible : bloque, mais une explosion la détruit
}
