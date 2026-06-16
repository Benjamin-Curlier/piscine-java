package piscine.m3;

/**
 * Exercice 3.1.3 — Membre.
 *
 * <p>Completez les methodes ci-dessous SANS modifier les signatures publiques :
 * les tests s'appuient dessus.</p>
 */
public class Membre {

    private final String nom;
    private final String niveau;
    private int pointsDeVie;

    /** Cree un membre avec un nom, un niveau et un nombre initial de points de vie. */
    public Membre(String nom, String niveau, int pointsDeVie) {
        this.nom = nom;
        this.niveau = niveau;
        // TODO : initialiser pointsDeVie
    }

    /** @return le nom du membre. */
    public String getNom() {
        return null; // TODO
    }

    /** @return le niveau du membre. */
    public String getNiveau() {
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
    public boolean estActif() {
        return false; // TODO
    }

    /**
     * @return une chaine de la forme {@code "<nom> (<niveau>) - <pv> PV"},
     *         ex. {@code "Martin (Confirmé) - 100 PV"}.
     */
    @Override
    public String toString() {
        return null; // TODO
    }
}
