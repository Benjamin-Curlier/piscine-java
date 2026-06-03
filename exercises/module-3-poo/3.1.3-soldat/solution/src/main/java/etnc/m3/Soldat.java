package etnc.m3;

/**
 * Exercice 3.1.3 — Soldat (solution de reference).
 *
 * <p>Illustre plusieurs methodes de comportement et un etat booleen derive
 * ({@link #estVivant}). Les points de vie ne descendent jamais sous 0.</p>
 */
public class Soldat {

    private final String nom;
    private final String grade;
    private int pointsDeVie;

    public Soldat(String nom, String grade, int pointsDeVie) {
        this.nom = nom;
        this.grade = grade;
        this.pointsDeVie = pointsDeVie;
    }

    public String getNom() {
        return nom;
    }

    public String getGrade() {
        return grade;
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
    public boolean estVivant() {
        return pointsDeVie > 0;
    }

    @Override
    public String toString() {
        return nom + " (" + grade + ") - " + pointsDeVie + " PV";
    }
}
