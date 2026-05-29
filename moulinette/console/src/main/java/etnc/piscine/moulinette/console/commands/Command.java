package etnc.piscine.moulinette.console.commands;

import etnc.piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public interface Command {
    String name();
    /** Texte court pour la commande {@code help}. */
    String shortHelp();
    CommandResult execute(ReplContext ctx, List<String> args);
}
