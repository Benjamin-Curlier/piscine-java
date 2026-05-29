package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;

import java.nio.file.Path;

public final class ReplContext {
    private final Path repoRoot;
    private final GitClient git;
    private final ExerciseCatalog catalog;
    private final Mode mode;

    public ReplContext(Path repoRoot, GitClient git, ExerciseCatalog catalog, Mode mode) {
        this.repoRoot = repoRoot; this.git = git; this.catalog = catalog; this.mode = mode;
    }
    public Path repoRoot() { return repoRoot; }
    public GitClient git() { return git; }
    public ExerciseCatalog catalog() { return catalog; }
    public Mode mode() { return mode; }
    public String currentBranch() { return git.currentBranch(repoRoot); }
}
