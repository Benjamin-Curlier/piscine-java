package etnc.m3;

/**
 * Exercice 3.1.3 — Soldat.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus.</p>
 */
public class Soldat {

    private final String nom;
    private final String grade;
    private int pointsDeVie;

    /** Cree un soldat avec un nom, un grade et un nombre initial de points de vie. */
    public Soldat(String nom, String grade, int pointsDeVie) {
        this.nom = nom;
        this.grade = grade;
        // TODO : initialiser pointsDeVie
    }

    /** @return le nom du soldat. */
    public String getNom() {
        return null; // TODO
    }

    /** @return le grade du soldat. */
    public String getGrade() {
        return null; // TODO
    }

    /** @return les points de vie courants. */
    public int getPointsDeVie() {
        return 0; // TODO
    }

    /** Retire {@code degats} points de vie, sans jamais descendre sous 0. */
    public void subirDegats(int degats) {
        // TODO
    }

    /** Rend {@code soin} points de vie. */
    public void soigner(int soin) {
        // TODO
    }

    /** @return vrai tant que les points de vie sont strictement positifs. */
    public boolean estVivant() {
        return false; // TODO
    }

    /**
     * @return une chaine de la forme {@code "<nom> (<grade>) - <pv> PV"},
     *         ex. {@code "Martin (Sergent) - 100 PV"}.
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
