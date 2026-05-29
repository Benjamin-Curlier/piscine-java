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

public class ProcessGitClient implements GitClient {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessGitClient.class);
    private static final long TIMEOUT_SECONDS = 30;

    private final Map<Path, List<RefUpdate>> lastPushByRepo = new HashMap<>();

    @Override
    public GitResult run(Path repo, List<String> args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.addAll(args);
        ProcessBuilder pb = new ProcessBuilder(cmd).directory(repo.toFile());
        pb.environment().put("LC_ALL", "C");
        try {
            Process p = pb.start();
            String stdout = readAll(p.getInputStream());
            String stderr = readAll(p.getErrorStream());
            if (!p.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                return new GitResult(-1, stdout, "timeout après " + TIMEOUT_SECONDS + "s");
            }
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
