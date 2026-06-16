package piscine.moulinette.console.commands;

import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;

import java.util.ArrayList;
import java.util.List;

public final class CommitCommand implements Command {
    @Override public String name() { return "commit"; }
    @Override public String shortHelp() { return "git commit -m \"...\" — enregistre les fichiers de l'index"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (!args.contains("-m")) {
            return CommandResult.error(
                "Un commit a besoin d'un message. Utilise : git commit -m \"ton message\"\n"
                + "exemple : git commit -m \"rendu 1.1.1\"\n");
        }
        List<String> full = new ArrayList<>();
        full.add("commit");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        return r.ok() ? CommandResult.ok(r.stdout()) : CommandResult.error(r.stderr());
    }
}
