package piscine.moulinette.console.commands;

import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.SubmissionTrigger;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Rend un sous-groupe en <strong>une</strong> commande : crée/bascule sur la branche
 * {@code rendu/<groupe>}, indexe tout, commite et pousse — ce qui déclenche la moulinette.
 *
 * <p>But : masquer git au débutant. Git reste enseigné au module 6, mais n'est plus un
 * prérequis dès le premier exercice : {@code submit 1.1} suffit.</p>
 */
public final class SubmitCommand implements Command {
    private static final Pattern GROUP = Pattern.compile("\\d+\\.\\d+");

    private final SubmissionTrigger trigger;

    public SubmitCommand(SubmissionTrigger trigger) { this.trigger = trigger; }

    @Override public String name() { return "submit"; }

    @Override public String shortHelp() {
        return "submit <sous-groupe> — rends tes exercices en une commande (ex: submit 1.1)";
    }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        String group;
        if (!args.isEmpty()) {
            group = args.get(0);
        } else {
            group = groupeCourant(ctx);
            if (group == null) {
                return CommandResult.error("usage : submit <sous-groupe>\nexemple : submit 1.1\n");
            }
        }
        if (!GROUP.matcher(group).matches()) {
            return CommandResult.error("format de sous-groupe invalide : " + group
                + " — attendu sous la forme X.Y (ex: 1.1, 2.3)\n");
        }
        String branche = "rendu/" + group;

        GitResult co = ctx.git().run(ctx.repoRoot(), List.of("checkout", "-B", branche));
        if (!co.ok()) {
            return CommandResult.error(co.stderr());
        }
        GitResult add = ctx.git().run(ctx.repoRoot(), List.of("add", "-A"));
        if (!add.ok()) {
            return CommandResult.error(add.stderr());
        }
        GitResult commit = ctx.git().run(ctx.repoRoot(), List.of("commit", "-m", "rendu " + group));
        if (!commit.ok() && !rienAValider(commit)) {
            return CommandResult.error(commit.stdout() + commit.stderr());
        }
        GitResult push = ctx.git().run(ctx.repoRoot(), List.of("push", "--porcelain", "origin", branche));
        if (!push.ok()) {
            return CommandResult.error(push.stderr());
        }

        StringBuilder out = new StringBuilder("Rendu du sous-groupe ").append(group).append(" envoyé.\n");
        if (trigger != null) {
            String t = trigger.onPushSucceeded(ctx);
            if (t != null) {
                out.append(t);
            }
        }
        return CommandResult.ok(out.toString());
    }

    /** Sous-groupe courant si la branche est de la forme {@code rendu/X.Y}, sinon {@code null}. */
    private static String groupeCourant(ReplContext ctx) {
        String b = ctx.git().currentBranch(ctx.repoRoot());
        return b != null && b.startsWith("rendu/") ? b.substring("rendu/".length()) : null;
    }

    /** Vrai si le commit a échoué simplement parce qu'il n'y avait rien à valider. */
    private static boolean rienAValider(GitResult commit) {
        String s = (commit.stdout() + " " + commit.stderr()).toLowerCase();
        return s.contains("nothing to commit") || s.contains("rien à valider")
            || s.contains("aucune modification");
    }
}
