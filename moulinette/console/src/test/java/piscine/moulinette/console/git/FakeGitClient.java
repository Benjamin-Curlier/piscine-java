package piscine.moulinette.console.git;

import java.nio.file.Path;
import java.util.*;

public class FakeGitClient implements GitClient {
    public final List<List<String>> calls = new ArrayList<>();
    public final Map<String, GitResult> stubbed = new HashMap<>();
    public String branch = "main";
    public List<RefUpdate> pushedRefs = List.of();

    @Override public GitResult run(Path repo, List<String> args) {
        calls.add(args);
        String key = String.join(" ", args);
        return stubbed.getOrDefault(key, new GitResult(0, "", ""));
    }
    @Override public String currentBranch(Path repo) { return branch; }
    @Override public List<RefUpdate> lastPushRefs(Path repo) { return pushedRefs; }

    public void stub(String argLine, GitResult r) { stubbed.put(argLine, r); }
}
