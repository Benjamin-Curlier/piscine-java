package piscine.moulinette.console.workspace;

import piscine.moulinette.console.git.ProcessGitClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("git")
class LocalWorkspaceInitializerIT {

    @Test
    void cree_repo_stagiaire_avec_bare_remote_et_starters(@TempDir Path tmp) throws IOException {
        Path piscine = tmp.resolve("piscine-java");
        Path exoDir = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Files.createDirectories(exoDir.resolve("starter/src/main/java"));
        Files.writeString(exoDir.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Files.writeString(exoDir.resolve("starter/src/main/java/Hello.java"), "class Hello{}");

        Path dest = tmp.resolve("piscine-curlier");
        var init = new LocalWorkspaceInitializer(new ProcessGitClient());

        Workspace ws = init.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        assertThat(ws.repoRoot()).isDirectory();
        assertThat(ws.bareRemote()).isDirectory();
        assertThat(ws.repoRoot().resolve(".git")).exists();
        assertThat(ws.repoRoot().resolve("exercises/1.1.1-hello-world/starter/src/main/java/Hello.java"))
            .exists();
        // remote origin = file:// vers le bare
        String config = Files.readString(ws.repoRoot().resolve(".git/config"));
        assertThat(config).contains(ws.bareRemote().toString().replace('\\', '/'));
    }
}
