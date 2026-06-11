package etnc.piscine.moulinette.console.git;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessGitClient implements GitClient {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessGitClient.class);
    private static final long TIMEOUT_SECONDS = 30;

    private final Map<Path, List<RefUpdate>> lastPushByRepo = new HashMap<>();

    /**
     * Exécutable git à utiliser : propriété système {@code piscine.git} (chemin complet,
     * positionnée par la GUI quand un MinGit est embarqué dans l'app installée) →
     * env {@code PISCINE_GIT_HOME} ({@code <home>/cmd/git.exe} ou {@code <home>/bin/git}) →
     * {@code git} du PATH.
     */
    public static String gitExecutable() {
        String prop = System.getProperty("piscine.git");
        if (prop != null && !prop.isBlank()) return prop;
        String home = System.getenv("PISCINE_GIT_HOME");
        if (home != null && !home.isBlank()) {
            Path cmd = Path.of(home, "cmd", "git.exe");
            if (java.nio.file.Files.isRegularFile(cmd)) return cmd.toString();
            Path bin = Path.of(home, "bin", "git");
            if (java.nio.file.Files.isRegularFile(bin)) return bin.toString();
        }
        return "git";
    }

    @Override
    public GitResult run(Path repo, List<String> args) {
        List<String> cmd = new ArrayList<>();
        cmd.add(gitExecutable());
        cmd.addAll(args);
        ProcessBuilder pb = new ProcessBuilder(cmd).directory(repo.toFile());
        pb.environment().put("LC_ALL", "C");
        try {
            Process p = pb.start();
            // On draine stderr dans un thread dédié : lire stdout jusqu'à EOF *puis* stderr
            // peut interbloquer si git remplit le buffer du pipe stderr (ex. MinGit émet un
            // avertissement CRLF par fichier sur `git add .`) pendant qu'on est bloqué sur stdout.
            AtomicReference<String> stderrRef = new AtomicReference<>("");
            Thread errDrain = new Thread(() -> {
                try {
                    stderrRef.set(readAll(p.getErrorStream()));
                } catch (IOException ignored) {
                    // flux fermé / process tué : stderr reste vide
                }
            }, "git-stderr-drain");
            errDrain.setDaemon(true);
            errDrain.start();
            String stdout = readAll(p.getInputStream());
            if (!p.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                errDrain.join(1000);
                return new GitResult(-1, stdout, "timeout après " + TIMEOUT_SECONDS + "s");
            }
            errDrain.join(1000);
            String stderr = stderrRef.get();
            GitResult res = new GitResult(p.exitValue(), stdout, stderr);
            if (!args.isEmpty() && "push".equals(args.get(0)) && res.ok()) {
                lastPushByRepo.put(repo.toAbsolutePath().normalize(), parsePushRefs(stdout));
            }
            LOG.debug("git {} -> exit={}", args, res.exitCode());
            return res;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            return new GitResult(-1, "", e.getMessage());
        }
    }

    @Override
    public String currentBranch(Path repo) {
        return run(repo, List.of("rev-parse", "--abbrev-ref", "HEAD")).stdout().trim();
    }

    @Override
    public List<RefUpdate> lastPushRefs(Path repo) {
        return lastPushByRepo.getOrDefault(repo.toAbsolutePath().normalize(), List.of());
    }

    private static String readAll(InputStream is) throws IOException {
        try (var r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line).append('\n');
            return sb.toString();
        }
    }

    /**
     * Parse la sortie de {@code git push --porcelain}.
     *
     * <p>Format porcelain (stdout) :
     * <pre>
     * To &lt;url&gt;
     * &lt;flag&gt;\t&lt;from&gt;:&lt;to&gt;\t&lt;summary&gt;
     * Done
     * </pre>
     * On ne garde que les lignes de ref (contenant {@code :refs/} dans le 2e champ tab-séparé).
     */
    static List<RefUpdate> parsePushRefs(String stdout) {
        List<RefUpdate> refs = new ArrayList<>();
        for (String line : stdout.split("\n")) {
            if (line.startsWith("To ") || line.equals("Done")) continue;
            String[] parts = line.split("\t");
            if (parts.length < 2) continue;
            String refspec = parts[1];
            int colon = refspec.indexOf(':');
            if (colon < 0) continue;
            String to = refspec.substring(colon + 1).trim();
            if (to.isEmpty()) continue;
            refs.add(new RefUpdate(to, "", ""));
        }
        return refs;
    }
}
