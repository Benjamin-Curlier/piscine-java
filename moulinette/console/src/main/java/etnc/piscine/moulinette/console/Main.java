package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.checkers.*;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.runner.ProcessRunner;
import etnc.piscine.moulinette.console.repl.CourseSiteServer;
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
        var toolkit = new JavaToolkit(new ProcessRunner());
        Path styleConfig = StyleChecker.extractBundledConfig();
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new MutationChecker(toolkit), // ne s'active que sur les exos « écriture de tests » (mutants/)
            new StyleChecker(toolkit, styleConfig));
        var runner = new MoulinetteRunner.Default(catalog, checkers, repo.resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        var repl = new Repl(ctx, CommandRegistry.defaults(trigger), ReplIo.stdio());

        String siteArg = optional(args, "--site", null);
        CourseSiteServer site = startSiteIfRequested(siteArg);
        try {
            repl.run();
        } finally {
            if (site != null) {
                site.stop();
            }
        }
        return 0;
    }

    private static CourseSiteServer startSiteIfRequested(String siteArg) {
        if (siteArg == null) {
            return null;
        }
        Path siteDir = Paths.get(siteArg);
        if (!java.nio.file.Files.isDirectory(siteDir)) {
            System.out.println("[console] Site de cours introuvable (" + siteDir
                + ") — démarrage sans site.");
            return null;
        }
        CourseSiteServer server = CourseSiteServer.start(siteDir, 8800, 8810);
        System.out.println("[console] Site de cours : " + server.url());
        tryOpenBrowser(server.url().toString());
        return server;
    }

    private static void tryOpenBrowser(String url) {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        List<String> cmd;
        if (os.contains("win")) {
            cmd = List.of("cmd", "/c", "start", "", url);
        } else if (os.contains("mac")) {
            cmd = List.of("open", url);
        } else {
            cmd = List.of("xdg-open", url);
        }
        try {
            new ProcessBuilder(cmd).inheritIO().start();
        } catch (Exception e) {
            System.out.println("[console] Ouvre le site manuellement : " + url);
        }
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
              moulinette-console repl --repo <dossier> [--piscine-repo <chemin>] [--site <dossier-site>] [--mode local]

            Documentation : docs/piscine-stagiaire.md
            """);
    }
}
