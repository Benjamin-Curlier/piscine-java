package piscine.m6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Petit utilitaire FOURNI (ne pas modifier) pour piloter Git depuis Java. Chaque appel
 * exécute une commande {@code git} dans le dépôt indiqué, exactement comme au terminal :
 *
 * <pre>{@code
 * GitCommandes g = new GitCommandes(repo);
 * g.run("add", "notes.txt");
 * g.run("commit", "-m", "Ajoute les notes");
 * String historique = g.log(); // "a1b2c3d Ajoute les notes"
 * }</pre>
 */
public final class GitCommandes {

    private final Path repo;

    public GitCommandes(Path repo) {
        this.repo = repo;
    }

    /** Le répertoire du dépôt (utile pour y créer des fichiers avec Files.writeString). */
    public Path repo() {
        return repo;
    }

    /** Lance {@code git <args>} et renvoie la sortie standard. Lève si la commande échoue. */
    public String run(String... args) {
        Resultat r = executer(args);
        if (r.code != 0) {
            throw new IllegalStateException("git " + String.join(" ", args)
                + " a échoué (code " + r.code + ") :\n" + r.stderr);
        }
        return r.stdout;
    }

    /** Variante tolérante : ne lève jamais (utile pour un merge qui peut renvoyer un conflit). */
    public Resultat tenter(String... args) {
        return executer(args);
    }

    /** Historique compact : {@code git log --oneline}. */
    public String log() {
        return run("log", "--oneline");
    }

    /** Nom de la branche courante. */
    public String brancheCourante() {
        return run("rev-parse", "--abbrev-ref", "HEAD").strip();
    }

    /** Résultat brut d'une commande git. */
    public record Resultat(int code, String stdout, String stderr) {}

    private Resultat executer(String... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        for (String a : args) {
            cmd.add(a);
        }
        try {
            Process p = new ProcessBuilder(cmd).directory(repo.toFile()).start();
            // stderr drainé dans un thread séparé pour éviter tout interblocage de buffer.
            AtomicReference<String> stderr = new AtomicReference<>("");
            Thread drain = new Thread(() -> stderr.set(lireTout(p.getErrorStream())), "git-stderr");
            drain.start();
            String stdout = lireTout(p.getInputStream());
            int code = p.waitFor();
            drain.join();
            return new Resultat(code, stdout, stderr.get());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Impossible d'exécuter git " + String.join(" ", args), e);
        }
    }

    private static String lireTout(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = r.readLine()) != null) {
                sb.append(ligne).append('\n');
            }
        } catch (IOException e) {
            // flux fermé : on renvoie ce qui a été lu
        }
        return sb.toString();
    }
}
