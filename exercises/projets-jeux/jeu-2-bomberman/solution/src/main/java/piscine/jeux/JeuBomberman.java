package piscine.jeux;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Cœur de logique d'un jeu façon Bomberman (solution de référence).
 *
 * <p>Le joueur se déplace sur le plateau et pose des bombes. À chaque {@code tick()}, les bombes
 * voient leur compteur diminuer ; à 0 elles explosent : le souffle se propage en croix sur
 * {@code portee} cases, s'arrête sur un MUR, détruit la première CAISSE rencontrée (et s'arrête là),
 * et tue le joueur s'il est dans le souffle.</p>
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

    /** Déplace le joueur si la case cible est dans le plateau et VIDE. Sans effet sinon, ou s'il est mort. */
    public void deplacer(Direction d) {
        if (!joueurVivant) {
            return;
        }
        Position cible = positionJoueur.plus(d);
        if (plateau.estDansPlateau(cible) && plateau.caseEn(cible) == Case.VIDE) {
            positionJoueur = cible;
        }
    }

    /** Pose une bombe à la position courante du joueur. */
    public void poserBombe(int compteur, int portee) {
        bombes.add(new Bombe(positionJoueur, compteur, portee));
    }

    /** Avance le temps d'un pas : décrémente les bombes, fait exploser celles arrivées à 0. */
    public void tick() {
        List<Bombe> restantes = new ArrayList<>();
        List<Bombe> aExploser = new ArrayList<>();
        for (Bombe b : bombes) {
            Bombe avancee = new Bombe(b.position(), b.compteur() - 1, b.portee());
            if (avancee.compteur() <= 0) {
                aExploser.add(avancee);
            } else {
                restantes.add(avancee);
            }
        }
        bombes = restantes;
        for (Bombe b : aExploser) {
            exploser(b);
        }
    }

    private void exploser(Bombe b) {
        Set<Position> souffle = new HashSet<>();
        souffle.add(b.position());
        for (Direction d : Direction.values()) {
            Position p = b.position();
            for (int i = 1; i <= b.portee(); i++) {
                p = p.plus(d);
                if (!plateau.estDansPlateau(p)) {
                    break;
                }
                Case c = plateau.caseEn(p);
                if (c == Case.MUR) {
                    break;                 // mur indestructible : bloque le souffle
                }
                souffle.add(p);
                if (c == Case.CAISSE) {
                    plateau.detruire(p);
                    break;                 // la caisse absorbe le souffle
                }
            }
        }
        if (souffle.contains(positionJoueur)) {
            joueurVivant = false;
        }
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
