package piscine.jeux;

import java.util.ArrayList;
import java.util.List;

/**
 * Cœur de logique d'un jeu façon Bomberman — À COMPLÉTER.
 *
 * <p>Vous implémentez deux méthodes : {@link #deplacer(Direction)} et {@link #tick()}.
 * Les champs, le constructeur, {@code poserBombe} et les accesseurs sont fournis ; vos méthodes
 * ont un accès direct aux champs privés (même classe).</p>
 */
public class JeuBomberman {

    private final Plateau plateau;
    private Position positionJoueur;
    private List<Bombe> bombes = new ArrayList<>();
    private boolean joueurVivant = true;

    public JeuBomberman(Plateau plateau, Position depart) {
        this.plateau = plateau;
        this.positionJoueur = depart;
    }

    /**
     * Déplace le joueur d'une case dans la direction donnée <strong>si</strong> la case cible
     * est dans le plateau ET vaut {@link Case#VIDE}. Sans effet sinon, ou si le joueur est mort.
     */
    public void deplacer(Direction d) {
        // TODO : implémenter.
        //  - rien si !joueurVivant
        //  - cible = positionJoueur.plus(d) ; bouger si plateau.estDansPlateau(cible)
        //    ET plateau.caseEn(cible) == Case.VIDE
    }

    /** Pose une bombe à la position courante du joueur. (FOURNI) */
    public void poserBombe(int compteur, int portee) {
        bombes.add(new Bombe(positionJoueur, compteur, portee));
    }

    /**
     * Avance le temps d'un pas :
     * <ol>
     *   <li>décrémente le {@code compteur} de chaque bombe ;</li>
     *   <li>toute bombe dont le compteur atteint 0 <strong>explose</strong> et disparaît.</li>
     * </ol>
     * Explosion d'une bombe : le souffle part de sa case et se propage en croix (les 4 directions)
     * sur {@code portee} cases. Dans chaque direction, il s'arrête sur un {@link Case#MUR}
     * (sans toucher la case du mur), et il <strong>détruit</strong> la première {@link Case#CAISSE}
     * rencontrée puis s'arrête. Si la case du joueur est dans le souffle, {@code joueurVivant}
     * devient {@code false}.
     */
    public void tick() {
        // TODO : implémenter (voir le sujet pour les règles détaillées de l'explosion).
    }

    public Plateau plateau() {
        return plateau;
    }

    public Position positionJoueur() {
        return positionJoueur;
    }

    public List<Bombe> bombes() {
        return List.copyOf(bombes);
    }

    public boolean joueurVivant() {
        return joueurVivant;
    }
}
