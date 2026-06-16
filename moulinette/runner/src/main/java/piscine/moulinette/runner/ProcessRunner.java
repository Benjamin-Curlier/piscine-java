package piscine.moulinette.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Exécute des commandes système (javac, java…) en processus forké.
 *
 * <p><strong>Version initiale :</strong> pas d'isolation réseau ni FS.
 * L'isolation renforcée (Docker, {@code --network=none}, FS read-only,
 * limites CPU/RAM, seccomp) est prévue en tâche <strong>#30</strong>.
 *
 * <p>Usage typique :
 * <pre>{@code
 * ProcessRunner runner = new ProcessRunner();
 * ProcessResult result = runner.run(
 *     Path.of("/rendu/etudiant"),
 *     List.of("javac", "-cp", ".", "HelloWorld.java")
 * );
 * if (!result.success()) {
 *     System.out.println(result.stderr());
 * }
 * }</pre>
 */
public class ProcessRunner {

    private static final Logger log = LoggerFactory.getLogger(ProcessRunner.class);

    /** Délai maximum d'exécution d'une commande (secondes). */
    private static final long TIMEOUT_SECONDS = 30L;

    /**
     * Exécute une commande dans le répertoire de travail donné.
     *
     * @param workDir répertoire dans lequel la commande est exécutée
     * @param command commande et ses arguments (ex. {@code ["javac", "Main.java"]})
     * @return résultat de l'exécution (code retour, stdout, stderr)
     * @throws IOException si le processus ne peut pas démarrer
     */
    public ProcessResult run(Path workDir, List<String> command) throws IOException {
        log.debug("Lancement de {} dans {}", command, workDir);

        ProcessBuilder pb = new ProcessBuilder(command)
                .directory(workDir.toFile());

        Process process = pb.start();
        String stdout;
        String stderr;

        try {
            // Lecture stdout et stderr avant waitFor pour éviter les deadlocks
            stdout = new String(process.getInputStream().readAllBytes());
            stderr = new String(process.getErrorStream().readAllBytes());

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                log.warn("Timeout ({} s) dépassé pour la commande {}", TIMEOUT_SECONDS, command);
                return new ProcessResult(-1, stdout, "TIMEOUT après " + TIMEOUT_SECONDS + "s\n" + stderr);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Exécution interrompue : " + command, e);
        }

        int exitCode = process.exitValue();
        log.debug("Code retour {} pour {}", exitCode, command);
        return new ProcessResult(exitCode, stdout, stderr);
    }
}
