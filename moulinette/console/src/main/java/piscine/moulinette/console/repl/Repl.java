package piscine.moulinette.console.repl;

import piscine.moulinette.console.ConsoleSession;
import piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;

public final class Repl {
    private final ConsoleSession session;
    private final ReplIo io;

    public Repl(ConsoleSession session, ReplIo io) {
        this.session = session; this.io = io;
    }

    public void run() throws IOException {
        io.write("Piscine Java — console locale.\n");
        io.write("  • `submit <sous-groupe>` (ex. `submit 1.1`) pour rendre un exercice et lancer la moulinette\n");
        io.write("  • `profil` pour voir ton XP, ton niveau et tes badges · `help` pour tout · `exit` pour quitter\n");
        while (true) {
            io.write("piscine[" + session.currentBranch() + "]> ");
            String line = io.readLine();
            if (line == null) return;
            line = line.trim();
            if (line.isEmpty()) continue;
            CommandResult r = session.execute(line);
            if (!r.output().isEmpty()) {
                io.write(r.output());
                if (!r.output().endsWith("\n")) io.write("\n");
            }
            if (r.shouldExit()) return;
        }
    }
}
