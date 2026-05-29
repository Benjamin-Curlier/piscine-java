package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class Repl {
    private final ReplContext ctx;
    private final CommandRegistry registry;
    private final ReplIo io;

    public Repl(ReplContext ctx, CommandRegistry registry, ReplIo io) {
        this.ctx = ctx; this.registry = registry; this.io = io;
    }

    public void run() throws IOException {
        io.write("Piscine ETNC — console locale. Tape `help` pour la liste, `exit` pour quitter.\n");
        while (true) {
            io.write("piscine[" + ctx.currentBranch() + "]> ");
            String line = io.readLine();
            if (line == null) return;
            line = line.trim();
            if (line.isEmpty()) continue;
            List<String> tokens = Arrays.stream(line.split("\\s+")).toList();
            CommandResult r = registry.dispatch(ctx, tokens);
            if (!r.output().isEmpty()) {
                io.write(r.output());
                if (!r.output().endsWith("\n")) io.write("\n");
            }
            if (r.shouldExit()) return;
        }
    }
}
