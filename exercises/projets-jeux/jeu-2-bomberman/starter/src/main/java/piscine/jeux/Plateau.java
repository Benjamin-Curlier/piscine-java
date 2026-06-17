package piscine.jeux;

/** FOURNI — ne pas modifier. Grille de {@link Case} (largeur colonnes x hauteur lignes, 0-indexé). */
public final class Plateau {

    private final int largeur;
    private final int hauteur;
    private final Case[][] grille; // [ligne][colonne]

    public Plateau(int largeur, int hauteur, Case[][] grille) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.grille = grille;
    }

    /**
     * Construit un plateau depuis une « carte » texte. Une chaîne = une ligne ;
     * {@code '#'} = MUR, {@code 'o'} = CAISSE, tout le reste = VIDE.
     */
    public static Plateau depuis(String... lignes) {
        int hauteur = lignes.length;
        int largeur = lignes[0].length();
        Case[][] g = new Case[hauteur][largeur];
        for (int l = 0; l < hauteur; l++) {
            for (int c = 0; c < largeur; c++) {
                g[l][c] = switch (lignes[l].charAt(c)) {
                    case '#' -> Case.MUR;
                    case 'o' -> Case.CAISSE;
                    default -> Case.VIDE;
                };
            }
        }
        return new Plateau(largeur, hauteur, g);
    }

    public int largeur() {
        return largeur;
    }

    public int hauteur() {
        return hauteur;
    }

    public boolean estDansPlateau(Position p) {
        return p.ligne() >= 0 && p.ligne() < hauteur
            && p.colonne() >= 0 && p.colonne() < largeur;
    }

    public Case caseEn(Position p) {
        return grille[p.ligne()][p.colonne()];
    }

    /** Transforme la case en VIDE (utilisé quand une caisse est soufflée). */
    public void detruire(Position p) {
        grille[p.ligne()][p.colonne()] = Case.VIDE;
    }
}
