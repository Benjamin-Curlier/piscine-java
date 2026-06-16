package piscine.moulinette.console.commands;

import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;

import java.util.ArrayList;
import java.util.List;

public final class DiffCommand implements Command {
    @Override public String name() { return "diff"; }
    @Override public String shortHelp() { return "git diff — montre les modifications non indexées"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        List<String> full = new ArrayList<>();
        full.add("diff");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
