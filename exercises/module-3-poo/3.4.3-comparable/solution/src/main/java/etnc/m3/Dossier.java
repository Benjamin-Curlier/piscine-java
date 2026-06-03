package etnc.m3;

/** Exercice 3.4.3 — Dossier (solution de reference) : implemente Ordonnable. */
public class Dossier implements Ordonnable {

    private final String titre;
    private final int priorite;

    public Dossier(String titre, int priorite) {
        this.titre = titre;
        this.priorite = priorite;
    }

    public String getTitre() {
        return titre;
    }

    public int getPriorite() {
        return priorite;
    }

    @Override
    public int comparerA(Ordonnable autre) {
        Dossier d = (Dossier) autre;
        return Integer.compare(this.priorite, d.priorite);
    }
}
