package piscine.m3;

/**
 * Exercice 3.1.3 — Membre (solution de reference).
 *
 * <p>Illustre plusieurs methodes de comportement et un etat booleen derive
 * ({@link #estActif}). Les points de vie ne descendent jamais sous 0.</p>
 */
public class Membre {

    private final String nom;
    private final String niveau;
    private int pointsDeVie;

    public Membre(String nom, String niveau, int pointsDeVie) {
        this.nom = nom;
        this.niveau = niveau;
        this.pointsDeVie = pointsDeVie;
    }

    public String getNom() {
        return nom;
    }

    public String getNiveau() {
        return niveau;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    /** Retire {@code degats} points de vie, sans descendre sous 0. */
    public void subirDegats(int degats) {
        this.pointsDeVie -= degats;
        if (this.pointsDeVie < 0) {
            this.pointsDeVie = 0;
        }
    }

    /** Rend {@code soin} points de vie. */
    public void soigner(int soin) {
        this.pointsDeVie += soin;
    }

    /** @return vrai tant que les points de vie sont strictement positifs. */
    public boolean estActif() {
        return pointsDeVie > 0;
    }

    @Override
    public String toString() {
        return nom + " (" + niveau + ") - " + pointsDeVie + " PV";
    }
}
