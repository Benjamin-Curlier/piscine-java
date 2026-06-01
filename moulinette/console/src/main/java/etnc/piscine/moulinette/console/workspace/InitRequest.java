package etnc.piscine.moulinette.console.workspace;

import java.nio.file.Path;

public record InitRequest(String nom, Path dest, Path piscineRepo, String moduleInitial) {}
