package etnc.piscine.moulinette.console.workspace;

import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.GitResult;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public final class LocalWorkspaceInitializer implements WorkspaceInitializer {

    private final GitClient git;

    public LocalWorkspaceInitializer(GitClient git) { this.git = git; }

    @Override
    public Workspace init(InitRequest req) {
        try {
            Path root = req.dest().toAbsolutePath().normalize();
            Path repoRoot = root;
            Path bareRoot = root.resolve(".piscine/remote.git");
            Files.createDirectories(repoRoot);
            Files.createDirectories(bareRoot);

            // remote bare (chemin en slashes pour git, cohérent multi-plateforme)
            String bareUrl = bareRoot.toString().replace('\\', '/');
            check(git.run(repoRoot, List.of("init", "--bare", bareUrl)), "bare init");

            check(git.run(repoRoot, List.of("init")), "repo init");
            check(git.run(repoRoot, List.of("config", "user.email", req.nom() + "@piscine.etnc")), "config email");
            check(git.run(repoRoot, List.of("config", "user.name", req.nom())), "config name");
            check(git.run(repoRoot, List.of("checkout", "-b", "main")), "checkout main");

            ExerciseCatalog cat = ExerciseCatalog.scan(req.piscineRepo().resolve("exercises"));
            Path exosDest = repoRoot.resolve("exercises");
            Files.createDirectories(exosDest);
            for (SousGroupe sg : cat.sousGroupes()) {
                for (ExerciseEntry e : sg.exercices()) {
                    Path target = exosDest.resolve(e.exerciseDir().getFileName());
                    copyTree(e.exerciseDir().resolve("starter"), target.resolve("starter"));
                    Files.copy(e.exerciseDir().resolve("metadata.yml"), target.resolve("metadata.yml"));
                }
            }
            Files.writeString(repoRoot.resolve("README.md"),
                "# Piscine ETNC — workspace de " + req.nom() + "\n\n"
                + "Voir docs/piscine-stagiaire.md dans le repo Piscine ETNC.\n");

            // .piscine/ contient le remote bare et les rapports : jamais versionné
            Files.writeString(repoRoot.resolve(".gitignore"), ".piscine/\n");

            check(git.run(repoRoot, List.of("add", ".")), "add");
            check(git.run(repoRoot, List.of("commit", "-m", "piscine: setup")), "commit initial");
            check(git.run(repoRoot, List.of("remote", "add", "origin", bareUrl)), "remote add");

            return new Workspace(root, bareRoot, repoRoot);
        } catch (IOException ioe) {
            throw new IllegalStateException("init workspace échoué", ioe);
        }
    }

    private static void check(GitResult r, String label) {
        if (!r.ok()) {
            throw new IllegalStateException("git " + label + " : exit=" + r.exitCode() + "\n" + r.stderr());
        }
    }

    private static void copyTree(Path src, Path dst) throws IOException {
        if (!Files.exists(src)) return;
        try (Stream<Path> walk = Files.walk(src)) {
            for (Path p : walk.toList()) {
                Path rel = src.relativize(p);
                Path target = dst.resolve(rel);
                if (Files.isDirectory(p)) Files.createDirectories(target);
                else {
                    Files.createDirectories(target.getParent());
                    Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
