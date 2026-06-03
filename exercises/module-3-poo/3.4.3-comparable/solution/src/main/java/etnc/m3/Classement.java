package etnc.m3;

/** Exercice 3.4.3 — Classement (solution de reference). */
public class Classement {

    /** Dossier de plus haute priorite, ou null si le tableau est vide. */
    public static Dossier plusPrioritaire(Dossier[] dossiers) {
        Dossier max = null;
        for (Dossier d : dossiers) {
            if (max == null || d.comparerA(max) > 0) {
                max = d;
            }
        }
        return max;
    }
}
