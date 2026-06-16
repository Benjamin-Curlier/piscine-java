package piscine.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utilitaire de test : alimente {@code System.in} avec un contenu fixe le temps
 * d'executer une action (typiquement un appel a main qui lit au clavier).
 *
 * <p>L'etat de {@code System.in} est restaure automatiquement, meme en cas
 * d'exception levee par l'action.</p>
 *
 * <pre>{@code
 * String[] sortie = new String[1];
 * CaptureEntree.avecEntree("Dupont\n42\n",
 *     () -> sortie[0] = CaptureSortie.capturer(() -> MaClasse.main(new String[]{})));
 * }</pre>
 */
public final class CaptureEntree {

    private CaptureEntree() {
        // classe utilitaire — pas d'instance
    }

    /**
     * Execute {@code action} en faisant croire au programme que l'utilisateur
     * a saisi {@code entree} au clavier.
     *
     * @param entree le texte saisi (lignes separees par '\n')
     * @param action code a executer (typiquement un appel a main)
     */
    public static void avecEntree(String entree, Runnable action) {
        InputStream original = System.in;
        try {
            System.setIn(new ByteArrayInputStream(entree.getBytes(StandardCharsets.UTF_8)));
            action.run();
        } finally {
            System.setIn(original);
        }
    }
}
