package etnc.piscine.moulinette.console.repl;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;
import java.util.ArrayList;
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
            List<String> tokens = tokenize(line);
            CommandResult r = registry.dispatch(ctx, tokens);
            if (!r.output().isEmpty()) {
                io.write(r.output());
                if (!r.output().endsWith("\n")) io.write("\n");
            }
            if (r.shouldExit()) return;
        }
    }

    /**
     * Découpe une ligne en jetons, en respectant les guillemets doubles.
     * Exemple : {@code git commit -m "mon message"} → [git, commit, -m, mon message].
     */
    static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        boolean hasToken = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                hasToken = true;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (hasToken) { tokens.add(cur.toString()); cur.setLength(0); hasToken = false; }
            } else {
                cur.append(c);
                hasToken = true;
            }
        }
        if (hasToken) tokens.add(cur.toString());
        return tokens;
    }
}
