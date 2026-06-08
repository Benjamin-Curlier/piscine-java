package etnc.m6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/** Utilitaire FOURNI pour piloter Git depuis Java (identique au starter). */
public final class GitCommandes {

    private final Path repo;

    public GitCommandes(Path repo) {
        this.repo = repo;
    }

    public Path repo() {
        return repo;
    }

    public String run(String... args) {
        Resultat r = executer(args);
        if (r.code != 0) {
            throw new IllegalStateException("git " + String.join(" ", args)
                + " a échoué (code " + r.code + ") :\n" + r.stderr);
        }
        return r.stdout;
    }

    public Resultat tenter(String... args) {
        return executer(args);
    }

    public String log() {
        return run("log", "--oneline");
    }

    public String brancheCourante() {
        return run("rev-parse", "--abbrev-ref", "HEAD").strip();
    }

    public record Resultat(int code, String stdout, String stderr) {}

    private Resultat executer(String... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        for (String a : args) {
            cmd.add(a);
        }
        try {
            Process p = new ProcessBuilder(cmd).directory(repo.toFile()).start();
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
