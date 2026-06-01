package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;

import java.util.ArrayList;
import java.util.List;

public final class AddCommand implements Command {
    @Override public String name() { return "add"; }
    @Override public String shortHelp() { return "git add <fichier> — ajoute des fichiers à l'index"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.isEmpty()) {
            return CommandResult.error(
                "usage : git add <fichier ou dossier>\n"
                + "exemple : git add exercises/1.1.1-hello-world\n");
        }
        List<String> full = new ArrayList<>();
        full.add("add");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
