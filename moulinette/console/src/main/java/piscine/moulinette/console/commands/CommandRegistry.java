package piscine.moulinette.console.commands;

import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.SubmissionTrigger;

import java.util.*;

public final class CommandRegistry {
    private final Map<String, Command> byName = new LinkedHashMap<>();

    private CommandRegistry() {}

    public static CommandRegistry defaults(SubmissionTrigger trigger) {
        var r = new CommandRegistry();
        r.register(new SubmitCommand(trigger)); // rendu en une commande (recommandé débutant)
        r.register(new ProfilCommand());        // XP / niveau / badges (gamification)
        r.register(new AddCommand());
        r.register(new CommitCommand());
        r.register(new PushCommand(trigger));
        r.register(new StatusCommand());
        r.register(new LogCommand());
        r.register(new DiffCommand());
        r.register(new SubmitStartCommand());
        r.register(new ExitCommand());
        r.register(new HelpCommand(r));
        return r;
    }

    public void register(Command c) { byName.put(c.name(), c); }
    public Collection<Command> all() { return byName.values(); }

    public CommandResult dispatch(ReplContext ctx, List<String> tokens) {
        if (tokens.isEmpty()) return CommandResult.ok("");
        List<String> rest = new ArrayList<>(tokens);
        // accepte "git X ..." comme "X ..."
        if ("git".equals(rest.get(0))) rest.remove(0);
        if (rest.isEmpty()) {
            return CommandResult.error("Tape `help` pour la liste des commandes.\n");
        }
        String head = rest.remove(0);
        Command c = byName.get(head);
        if (c == null) {
            return CommandResult.error(unsupportedMessage(head));
        }
        return c.execute(ctx, rest);
    }

    private static String unsupportedMessage(String head) {
        String hint = switch (head) {
            case "checkout", "branch" -> "Pour rendre tes exercices, utilise `submit <sous-groupe>` (ex: submit 1.1).";
            case "clone", "pull", "fetch", "merge", "rebase" -> "Cette commande n'est pas couverte par le MVP.";
            default -> "Tape `help` pour voir les commandes supportées.";
        };
        return "Commande non supportée dans le MVP : " + head + "\n" + hint + "\n";
    }
}
