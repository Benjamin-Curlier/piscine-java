package piscine.moulinette.console.commands;

import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;

import java.util.List;
import java.util.regex.Pattern;

public final class SubmitStartCommand implements Command {
    private static final Pattern GROUP = Pattern.compile("\\d+\\.\\d+");

    @Override public String name() { return "submit-start"; }
    @Override public String shortHelp() { return "submit-start <sous-groupe> — bascule sur la branche rendu/<sous-groupe>"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.isEmpty()) {
            return CommandResult.error("usage : submit-start <sous-groupe>\nexemple : submit-start 1.1\n");
        }
        String group = args.get(0);
        if (!GROUP.matcher(group).matches()) {
            return CommandResult.error("format de sous-groupe invalide : " + group
                + " — attendu sous la forme X.Y (ex: 1.1, 2.3)\n");
        }
        GitResult r = ctx.git().run(ctx.repoRoot(), List.of("checkout", "-B", "rendu/" + group));
        return r.ok()
            ? CommandResult.ok("Bascule sur la branche rendu/" + group + ".\n")
            : CommandResult.error(r.stderr());
    }
}
