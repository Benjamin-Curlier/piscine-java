package etnc.piscine.moulinette.console.git;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("git")
class ProcessGitClientIT {

    @Test
    void init_commit_push_vers_bare_local(@TempDir Path tmp) throws IOException {
        Path repo = tmp.resolve("work");
        Path bare = tmp.resolve("remote.git");
        Files.createDirectories(repo);
        ProcessGitClient git = new ProcessGitClient();

        // bare remote
        assertThat(git.run(tmp, List.of("init", "--bare", bare.toString())).ok()).isTrue();

        // working repo
        assertThat(git.run(repo, List.of("init")).ok()).isTrue();
        assertThat(git.run(repo, List.of("config", "user.email", "test@etnc")).ok()).isTrue();
        assertThat(git.run(repo, List.of("config", "user.name", "test")).ok()).isTrue();
        assertThat(git.run(repo, List.of("checkout", "-b", "main")).ok()).isTrue();

        Files.writeString(repo.resolve("README.md"), "hello");
        assertThat(git.run(repo, List.of("add", ".")).ok()).isTrue();
        assertThat(git.run(repo, List.of("commit", "-m", "initial")).ok()).isTrue();

        // push
        assertThat(git.run(repo, List.of("remote", "add", "origin", bare.toString())).ok()).isTrue();
        GitResult push = git.run(repo, List.of("push", "--porcelain", "origin", "main"));
        assertThat(push.ok()).as("push stderr=%s stdout=%s", push.stderr(), push.stdout()).isTrue();

        assertThat(git.currentBranch(repo)).isEqualTo("main");
        assertThat(git.lastPushRefs(repo))
            .anySatisfy(r -> assertThat(r.ref()).isEqualTo("refs/heads/main"));
    }
}
