package piscine.jeux;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

/**
 * Cœur de logique du jeu Snake — À COMPLÉTER.
 *
 * <p>Vous devez implémenter deux méthodes : {@link #changerDirection(Direction)} et
 * {@link #avancer()}. Les champs, le constructeur et les accesseurs sont fournis ;
 * vos méthodes ont un accès direct aux champs privés (même classe).</p>
 *
 * <p>Plateau de {@code largeur} colonnes x {@code hauteur} lignes, 0-indexé.
 * Le serpent est une file de cases, tête en tête ({@code serpent.peekFirst()}).</p>
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

    /**
     * Change la direction du serpent. Le demi-tour (direction opposée à la direction
     * courante) est <strong>ignoré</strong> dès que le serpent a un corps (longueur &gt; 1).
     * Sans effet si la partie est terminée.
     */
    public void changerDirection(Direction nouvelle) {
        // TODO : implémenter.
        //  - rien à faire si la partie est terminée (termine)
        //  - ignorer le demi-tour si serpent.size() > 1 et nouvelle == direction.opposee()
        //  - sinon : mettre à jour `direction`
    }

    /**
     * Avance d'un pas. Calcule la nouvelle tête = tête courante + direction, puis :
     * <ul>
     *   <li>hors du plateau (ligne/colonne &lt; 0 ou &ge; hauteur/largeur) → partie terminée ;</li>
     *   <li>collision avec le corps → partie terminée. <em>Subtilité</em> : quand on ne mange
     *       pas, la queue libère sa case ; y entrer est donc permis ;</li>
     *   <li>la nouvelle tête tombe sur la pomme → le serpent grandit (la queue reste),
     *       {@code score} augmente de 1, et une nouvelle pomme est tirée
     *       ({@code generateurPommes.get()}) ;</li>
     *   <li>sinon → le serpent avance (ajouter la tête, retirer la queue).</li>
     * </ul>
     * Sans effet si la partie est déjà terminée.
     */
    public void avancer() {
        // TODO : implémenter le pas de jeu (voir le sujet pour les règles détaillées).
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
