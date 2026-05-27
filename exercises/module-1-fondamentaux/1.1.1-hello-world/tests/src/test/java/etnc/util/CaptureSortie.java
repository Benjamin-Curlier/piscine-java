package etnc.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Utilitaire de test : capture ce qu'une portion de code ecrit sur
 * {@code System.out} et restitue la chaine capturee.
 *
 * <p>L'etat de {@code System.out} est restaure automatiquement,
 * meme en cas d'exception levee par l'action.</p>
 *
 * <p>Exemple d'utilisation dans un test :</p>
 *
 * <pre>{@code
 * String sortie = CaptureSortie.capturer(() -> MaClasse.main(new String[]{}));
 * assertThat(sortie).isEqualTo("attendu" + System.lineSeparator());
 * }</pre>
 */
public final class CaptureSortie {

    private CaptureSortie() {
        // classe utilitaire — pas d'instance
    }

    /**
     * Execute l'action passee en parametre en capturant tout ce qui est
     * ecrit sur la sortie standard pendant son execution.
     *
     * @param action code a executer (typiquement une lambda appelant main)
     * @return le contenu ecrit sur System.out par l'action, en UTF-8
     */
    public static String capturer(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            action.run();
            System.out.flush();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
