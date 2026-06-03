package etnc.m3;

/**
 * Exercice 3.4.4 — Journal (solution de reference) : resume un evenement par un
 * switch exhaustif sur une hierarchie sealed (sans default).
 */
public class Journal {

    public static String resumer(Evenement e) {
        return switch (e) {
            case Connexion c -> "Connexion de " + c.utilisateur();
            case Deconnexion d -> "Deconnexion de " + d.utilisateur();
            case Erreur err -> "Erreur " + err.code() + " : " + err.message();
        };
    }
}
