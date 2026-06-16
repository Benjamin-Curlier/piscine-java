package piscine.moulinette.gui;

import piscine.moulinette.console.ConsoleSession;
import piscine.moulinette.console.git.ProcessGitClient;
import piscine.moulinette.console.repl.CourseSiteServer;
import piscine.moulinette.console.workspace.InitRequest;
import piscine.moulinette.console.workspace.LocalWorkspaceInitializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Point d'entrée GUI : démarre le serveur local et ouvre le navigateur.
 *
 * <p>Sans argument : mode « installé » (jpackage) — contenu piscine et MinGit cherchés à
 * côté du jar ({@code <app>/piscine}, {@code <app>/git}), workspace auto-initialisé au
 * premier lancement dans {@code ~/PiscineJava/workspace} (surchargeable env {@code PISCINE_HOME}).
 */
public final class Main {

    public static void main(String[] args) throws Exception {
        List<String> a = List.of(args);
        if (a.contains("--help")) { printUsage(); System.exit(0); }

        Path repo;
        Path piscine;
        if (a.contains("--repo")) {
            repo = Paths.get(required(a, "--repo"));
            Path defaultPiscine = repo.toAbsolutePath().normalize().getParent().resolve("piscine-java");
            piscine = Paths.get(optional(a, "--piscine-repo", defaultPiscine.toString()));
        } else {
            Path appDir = appDir();
            piscine = appDir.resolve("piscine");
            if (!Files.isDirectory(piscine.resolve("exercises"))) {
                System.err.println("[gui] Contenu piscine introuvable (" + piscine
                    + ") — en dev, passe --repo/--piscine-repo.");
                printUsage();
                System.exit(2);
            }
            Path gitHome = appDir.resolve("git");
            Path gitExe = gitHome.resolve("cmd/git.exe");
            if (Files.isRegularFile(gitExe)) {
                System.setProperty("piscine.git", gitExe.toString());
            }
            String home = System.getenv("PISCINE_HOME");
            Path dataDir = home != null && !home.isBlank()
                ? Paths.get(home)
                : Paths.get(System.getProperty("user.home"), "PiscineJava");
            repo = dataDir.resolve("workspace");
            if (!Files.isDirectory(repo.resolve(".piscine"))) {
                System.out.println("[gui] Premier lancement : initialisation du workspace dans " + repo);
                new LocalWorkspaceInitializer(new ProcessGitClient())
                    .init(new InitRequest(System.getProperty("user.name", "stagiaire"),
                        repo, piscine, "module-1-fondamentaux"));
            }
        }

        int port = Integer.parseInt(optional(a, "--port", "0"));
        String siteArg = optional(a, "--site", null);
        Path site = siteArg != null ? Paths.get(siteArg) : appDir().resolve("piscine/site");
        if (!Files.isDirectory(site)) {
            if (siteArg != null) {
                System.out.println("[gui] Site de cours introuvable (" + site + ") — démarrage sans cours.");
            }
            site = null;
        }

        ConsoleSession session = ConsoleSession.open(repo, piscine);
        // Le site Docusaurus (baseUrl '/') est servi à la racine de son propre serveur.
        CourseSiteServer siteServer = site != null ? CourseSiteServer.start(site, 8800, 8899) : null;
        String coursesUrl = siteServer != null ? siteServer.url().toString() : null;
        GuiServer server = GuiServer.start(session, port, coursesUrl);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            if (siteServer != null) siteServer.stop();
        }));

        System.out.println("[gui] Piscine Java : " + server.url());
        tryOpenBrowser(server.url().toString());
        if (!installTrayIcon(server)) {
            // pas de zone de notification (Linux headless…) : on reste vivant en console
            System.out.println("[gui] Ctrl-C pour quitter.");
        }
        Thread.currentThread().join();
    }

    /**
     * Icône de zone de notification : l'app installée n'a aucune fenêtre (jpackage sans
     * console) — le tray est le seul point de contrôle visible (ouvrir / quitter).
     * Rend false si le bureau ne propose pas de tray.
     */
    private static boolean installTrayIcon(GuiServer server) {
        try {
            if (!java.awt.SystemTray.isSupported()) return false;
            java.awt.PopupMenu menu = new java.awt.PopupMenu();
            java.awt.MenuItem open = new java.awt.MenuItem("Ouvrir Piscine Java");
            open.addActionListener(e -> tryOpenBrowser(server.url().toString()));
            java.awt.MenuItem quit = new java.awt.MenuItem("Quitter");
            quit.addActionListener(e -> System.exit(0));
            menu.add(open);
            menu.addSeparator();
            menu.add(quit);
            java.awt.TrayIcon icon = new java.awt.TrayIcon(trayImage(), "Piscine Java — " + server.url(), menu);
            icon.setImageAutoSize(true);
            icon.addActionListener(e -> tryOpenBrowser(server.url().toString())); // double-clic
            java.awt.SystemTray.getSystemTray().add(icon);
            return true;
        } catch (Exception | UnsatisfiedLinkError e) {
            return false;
        }
    }

    /** Icône 16×16 dessinée à la volée (un « P » sur pastille bleue) : zéro ressource binaire. */
    private static java.awt.Image trayImage() {
        var img = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        var g = img.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new java.awt.Color(0x89, 0xB4, 0xFA));
        g.fillOval(0, 0, 16, 16);
        g.setColor(java.awt.Color.WHITE);
        g.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 11));
        g.drawString("P", 5, 12);
        g.dispose();
        return img;
    }

    /** Dossier contenant le jar de l'app (mode installé) ; le cwd en fallback (dev). */
    private static Path appDir() {
        try {
            Path p = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return p.toString().endsWith(".jar") ? p.getParent() : Paths.get(".");
        } catch (Exception e) {
            return Paths.get(".");
        }
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
            Piscine Java — interface locale

            Usage :
              moulinette-gui                                  # mode installé : workspace auto (~/PiscineJava)
              moulinette-gui --repo <workspace> [--piscine-repo <chemin>] [--site <dossier-site>] [--port <n>]
            """);
    }
}
