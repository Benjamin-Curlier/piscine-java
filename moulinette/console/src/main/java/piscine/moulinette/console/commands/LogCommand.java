package piscine.moulinette.console.commands;

import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public final class LogCommand implements Command {
    @Override public String name() { return "log"; }
    @Override public String shortHelp() { return "git log — historique condensé (20 derniers commits)"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        GitResult r = ctx.git().run(ctx.repoRoot(), List.of("log", "--oneline", "-n", "20"));
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
