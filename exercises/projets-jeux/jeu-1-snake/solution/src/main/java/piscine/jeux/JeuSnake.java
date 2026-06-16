package piscine.jeux;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

/**
 * Cœur de logique du jeu Snake (solution de référence).
 *
 * <p>Plateau de {@code largeur} colonnes x {@code hauteur} lignes, 0-indexé.
 * Le serpent est une file de cases, tête en tête. La pomme suivante est fournie
 * par un générateur (injecté), ce qui rend la logique déterministe pour les tests.</p>
 */
public class JeuSnake {

    private final int largeur;
    private final int hauteur;
    private final Deque<Position> serpent = new ArrayDeque<>();
    private final Supplier<Position> generateurPommes;
    private Direction direction;
    private Position pomme;
    private int score = 0;
    private boolean termine = false;

    public JeuSnake(int largeur, int hauteur, Position depart,
                    Direction directionInitiale, Supplier<Position> generateurPommes) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.direction = directionInitiale;
        this.generateurPommes = generateurPommes;
        this.serpent.addFirst(depart);
        this.pomme = generateurPommes.get();
    }

    public void changerDirection(Direction nouvelle) {
        if (termine) {
            return;
        }
        // Demi-tour interdit dès que le serpent a un corps.
        if (serpent.size() > 1 && nouvelle == direction.opposee()) {
            return;
        }
        this.direction = nouvelle;
    }

    public void avancer() {
        if (termine) {
            return;
        }
        Position prochaine = serpent.peekFirst().plus(direction);

        if (horsPlateau(prochaine)) {
            termine = true;
            return;
        }

        boolean mange = prochaine.equals(pomme);
        // Sans croissance, la queue libère sa case : y entrer est permis.
        boolean collision = mange ? serpent.contains(prochaine)
                                  : corpsSansQueue().contains(prochaine);
        if (collision) {
            termine = true;
            return;
        }

        serpent.addFirst(prochaine);
        if (mange) {
            score++;
            pomme = generateurPommes.get();
        } else {
            serpent.removeLast();
        }
    }

    private boolean horsPlateau(Position p) {
        return p.ligne() < 0 || p.ligne() >= hauteur
            || p.colonne() < 0 || p.colonne() >= largeur;
    }

    private List<Position> corpsSansQueue() {
        List<Position> liste = new ArrayList<>(serpent);
        if (!liste.isEmpty()) {
            liste.remove(liste.size() - 1);
        }
        return liste;
    }

    public boolean estTermine() {
        return termine;
    }

    public int score() {
        return score;
    }

    public int largeur() {
        return largeur;
    }

    public int hauteur() {
        return hauteur;
    }

    public Direction direction() {
        return direction;
    }

    public Position tete() {
        return serpent.peekFirst();
    }

    public Position pomme() {
        return pomme;
    }

    public int longueur() {
        return serpent.size();
    }

    /** Copie immuable du corps, tête en premier. */
    public List<Position> corps() {
        return List.copyOf(serpent);
    }
}
