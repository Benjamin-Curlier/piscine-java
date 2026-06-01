package etnc.piscine.moulinette.console.git;

import java.nio.file.Path;
import java.util.List;

public interface GitClient {
    GitResult run(Path repo, List<String> args);
    String currentBranch(Path repo);
    /** Refs poussées par le dernier {@code git push} capturé dans ce client. Vide si pas de push. */
    List<RefUpdate> lastPushRefs(Path repo);
}
