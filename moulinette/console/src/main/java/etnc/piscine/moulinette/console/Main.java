package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.Repl;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.repl.ReplIo;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Main {

    public static void main(String[] args) {
        try {
            int exit = new Main().dispatch(Arrays.asList(args));
            System.exit(exit);
        } catch (ConsoleException ce) {
            System.err.println("Erreur : " + ce.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Bug interne : " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(3);
        }
    }

    int dispatch(List<String> args) throws Exception {
        if (args.isEmpty() || "--help".equals(args.get(0))) { printUsage(); return 0; }
        String cmd = args.get(0);
        List<String> rest = args.subList(1, args.size());
        Mode mode = parseMode(rest);
        if (mode == Mode.NOMINAL) {
            throw new ConsoleException(
                "--mode nominal non implémenté dans le MVP (voir tâches #26-#28 du backlog).");
        }
        return switch (cmd) {
            case "init" -> runInit(rest);
            case "repl" -> runRepl(rest);
            default -> { printUsage(); yield 1; }
        };
    }

    private int runInit(List<String> args) {
        String nom = required(args, "--nom");
        Path dest = Paths.get(required(args, "--dest"));
        Path piscine = Paths.get(optional(args, "--piscine-repo", "."));
        var init = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = init.init(new InitRequest(nom, dest, piscine, "module-1-fondamentaux"));
        System.out.println("[console] Workspace prêt : " + ws.repoRoot());
        System.out.println("[console] Lance le REPL avec :");
        System.out.println("  java -jar moulinette-console.jar repl --repo " + ws.repoRoot()
            + " --piscine-repo " + piscine.toAbsolutePath().normalize());
        return 0;
    }

    private int runRepl(List<String> args) throws Exception {
        Path repo = Paths.get(required(args, "--repo"));
        Path defaultPiscine = repo.toAbsolutePath().normalize().getParent().resolve("piscine-etnc");
        Path piscine = Paths.get(optional(args, "--piscine-repo", defaultPiscine.toString()));
        GitClient git = new ProcessGitClient();
        ExerciseCatalog catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        var runner = new MoulinetteRunner.Default(catalog, List.of(), repo.resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        new Repl(ctx, CommandRegistry.defaults(trigger), ReplIo.stdio()).run();
        return 0;
    }

    private static Mode parseMode(List<String> args) {
        int i = args.indexOf("--mode");
        if (i < 0 || i + 1 >= args.size()) return Mode.LOCAL;
        return Mode.valueOf(args.get(i + 1).toUpperCase(Locale.ROOT));
    }

    private static String required(List<String> args, String flag) {
        int i = args.indexOf(flag);
        if (i < 0 || i + 1 >= args.size())
            throw new ConsoleException("argument requis manquant : " + flag);
        return args.get(i + 1);
    }

    private static String optional(List<String> args, String flag, String def) {
        int i = args.indexOf(flag);
        return (i < 0 || i + 1 >= args.size()) ? def : args.get(i + 1);
    }

    private static void printUsage() {
        System.out.println("""
            Moulinette ETNC — console locale

            Usage :
              moulinette-console init --nom <slug> --dest <dossier> [--piscine-repo <chemin>] [--mode local]
              moulinette-console repl --repo <dossier> [--piscine-repo <chemin>] [--mode local]

            Documentation : docs/piscine-stagiaire.md
            """);
    }
}
