package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;
import java.util.stream.Collectors;

public final class HelpCommand implements Command {
    private final CommandRegistry registry;
    public HelpCommand(CommandRegistry r) { this.registry = r; }
    @Override public String name() { return "help"; }
    @Override public String shortHelp() { return "help — affiche les commandes supportées"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) {
        String body = registry.all().stream()
            .map(c -> "  " + c.shortHelp())
            .collect(Collectors.joining("\n"));
        return CommandResult.ok("Commandes supportées :\n" + body + "\n");
    }
}
