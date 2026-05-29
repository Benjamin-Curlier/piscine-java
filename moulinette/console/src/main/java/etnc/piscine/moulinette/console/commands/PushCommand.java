package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;

import java.util.ArrayList;
import java.util.List;

public final class PushCommand implements Command {
    private final SubmissionTrigger trigger;

    public PushCommand(SubmissionTrigger trigger) { this.trigger = trigger; }

    @Override public String name() { return "push"; }
    @Override public String shortHelp() { return "git push <remote> <branche> — pousse la branche vers le remote"; }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (args.size() < 2) {
            return CommandResult.error(
                "usage : git push <remote> <branche>\n"
                + "exemple : git push origin rendu/1.1\n");
        }
        List<String> full = new ArrayList<>();
        full.add("push"); full.add("--porcelain");
        full.addAll(args);
        GitResult r = ctx.git().run(ctx.repoRoot(), full);
        if (!r.ok()) return CommandResult.error(r.stderr());
        StringBuilder out = new StringBuilder(r.stdout());
        if (trigger != null) {
            String triggerOut = trigger.onPushSucceeded(ctx);
            if (triggerOut != null) out.append(triggerOut);
        }
        return CommandResult.ok(out.toString());
    }
}
