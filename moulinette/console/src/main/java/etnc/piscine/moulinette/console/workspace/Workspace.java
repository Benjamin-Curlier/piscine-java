package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;

public record Workspace(Path root, Path bareRemote, Path repoRoot) {}
