package etnc.m3;

/**
 * Exercice 3.4.4 — Journal : resume un evenement.
 *
 * <p>La hierarchie Evenement (sealed) et ses records sont FOURNIS. Completez
 * resumer avec un switch exhaustif SANS default.</p>
 */
public class Journal {

    /**
     * @return le resume de l'evenement :
     *         Connexion -> "Connexion de <utilisateur>",
     *         Deconnexion -> "Deconnexion de <utilisateur>",
     *         Erreur -> "Erreur <code> : <message>".
     */
    public static String resumer(Evenement e) {
        return null; // TODO : switch exhaustif (case Connexion c -> ...), sans default
    }
}
