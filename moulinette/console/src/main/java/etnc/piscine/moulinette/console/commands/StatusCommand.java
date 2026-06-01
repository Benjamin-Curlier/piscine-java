package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public final class StatusCommand implements Command {
    @Override public String name() { return "status"; }
    @Override public String shortHelp() { return "git status — montre l'état du repo"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        GitResult r = ctx.git().run(ctx.repoRoot(), List.of("status"));
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
