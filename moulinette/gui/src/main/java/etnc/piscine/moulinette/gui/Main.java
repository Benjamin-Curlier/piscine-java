package etnc.piscine.moulinette.gui;

import etnc.piscine.moulinette.console.ConsoleSession;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/** Point d'entrée GUI : démarre le serveur local et ouvre le navigateur. */
public final class Main {

    public static void main(String[] args) throws Exception {
        List<String> a = List.of(args);
        if (a.isEmpty() || a.contains("--help")) { printUsage(); System.exit(a.isEmpty() ? 1 : 0); }
        Path repo = Paths.get(required(a, "--repo"));
        Path defaultPiscine = repo.toAbsolutePath().normalize().getParent().resolve("piscine-etnc");
        Path piscine = Paths.get(optional(a, "--piscine-repo", defaultPiscine.toString()));

        int port = Integer.parseInt(optional(a, "--port", "0"));
        String siteArg = optional(a, "--site", null);
        Path site = null;
        if (siteArg != null) {
            site = Paths.get(siteArg);
            if (!java.nio.file.Files.isDirectory(site)) {
                System.out.println("[gui] Site de cours introuvable (" + site + ") — démarrage sans cours.");
                site = null;
            }
        }

        ConsoleSession session = ConsoleSession.open(repo, piscine);
        GuiServer server = GuiServer.start(session, port, site);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        System.out.println("[gui] Piscine ETNC : " + server.url());
        System.out.println("[gui] Laisse cette fenêtre ouverte ; Ctrl-C pour quitter.");
        tryOpenBrowser(server.url().toString());
        Thread.currentThread().join();
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
            System.out.println("[gui] Ouvre la page manuellement : " + url);
        }
    }

    private static String required(List<String> args, String flag) {
        int i = args.indexOf(flag);
        if (i < 0 || i + 1 >= args.size()) {
            System.err.println("argument requis manquant : " + flag);
            printUsage();
            System.exit(2);
        }
        return args.get(i + 1);
    }

    private static String optional(List<String> args, String flag, String def) {
        int i = args.indexOf(flag);
        return (i < 0 || i + 1 >= args.size()) ? def : args.get(i + 1);
    }

    private static void printUsage() {
        System.out.println("""
            Piscine ETNC — interface locale

            Usage :
              moulinette-gui --repo <workspace> [--piscine-repo <chemin>] [--site <dossier-site>] [--port <n>]
            """);
    }
}
