package etnc.piscine.moulinette.gui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import etnc.piscine.moulinette.console.ConsoleSession;
import etnc.piscine.moulinette.console.commands.CommandResult;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.workspace.ExerciseEntry;
import etnc.piscine.moulinette.console.workspace.SousGroupe;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Serveur HTTP local de la GUI : expose une {@link ConsoleSession} en JSON et sert
 * le frontend statique embarqué (classpath {@code /web}). Lié à 127.0.0.1 uniquement.
 */
public final class GuiServer {

    private static final Pattern REPORT_NAME = Pattern.compile("[A-Za-z0-9._-]+\\.md");

    private final HttpServer server;
    private final ConsoleSession session;
    private final Path siteDir; // site de cours (Docusaurus construit), peut être null

    private GuiServer(HttpServer server, ConsoleSession session, Path siteDir) {
        this.server = server;
        this.session = session;
        this.siteDir = siteDir;
    }

    /** Démarre sur {@code port} (0 = port éphémère libre). */
    public static GuiServer start(ConsoleSession session, int port) {
        return start(session, port, null);
    }

    /** Variante avec site de cours statique monté sous {@code /cours/}. */
    public static GuiServer start(ConsoleSession session, int port, Path siteDir) {
        try {
            var addr = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
            HttpServer s = HttpServer.create(addr, 0);
            GuiServer gui = new GuiServer(s, session, siteDir);
            s.createContext("/", gui::handleStatic);
            s.createContext("/api/terminal", gui::handleTerminal);
            s.createContext("/api/state", gui::handleState);
            s.createContext("/api/progress", gui::handleProgress);
            s.createContext("/api/reports", gui::handleReports);
            s.createContext("/api/report", gui::handleReport);
            s.createContext("/api/open", gui::handleOpen);
            if (siteDir != null) {
                s.createContext("/cours", gui::handleCours);
            }
            s.start();
            return gui;
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de démarrer le serveur GUI", e);
        }
    }

    public int port() {
        return server.getAddress().getPort();
    }

    public URI url() {
        return URI.create("http://127.0.0.1:" + port() + "/");
    }

    public void stop() {
        server.stop(0);
    }

    private void handleTerminal(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) { respond(ex, 405, "text/plain", "POST attendu"); return; }
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String cmd = Json.stringField(body, "cmd");
        if (cmd == null) { respond(ex, 400, "application/json", "{\"error\":\"champ cmd manquant\"}"); return; }
        CommandResult r;
        try {
            r = session.execute(cmd);
        } catch (Exception e) {
            respond(ex, 200, "application/json",
                "{\"exitCode\":3,\"output\":\"Bug interne : " + Json.escape(String.valueOf(e.getMessage()))
                    + "\",\"shouldExit\":false}");
            return;
        }
        respond(ex, 200, "application/json",
            "{\"exitCode\":" + r.exitCode()
                + ",\"output\":\"" + Json.escape(r.output())
                + "\",\"shouldExit\":" + r.shouldExit() + "}");
    }

    private void handleState(HttpExchange ex) throws IOException {
        respond(ex, 200, "application/json",
            "{\"branch\":\"" + Json.escape(session.currentBranch())
                + "\",\"repoRoot\":\"" + Json.escape(session.repoRoot().toString())
                + "\",\"courses\":" + (siteDir != null) + "}");
    }

    /**
     * Progression séquentielle : exos triés (sous-groupes croissants, position croissante) ;
     * statut {@code done} si présent dans progress.json, le premier non-validé est
     * {@code current}, tout ce qui suit est {@code locked}.
     */
    private void handleProgress(HttpExchange ex) throws IOException {
        if (session.catalog() == null) { respond(ex, 200, "application/json", "{\"modules\":[]}"); return; }
        Set<String> valides = MoulinetteRunner.Default.lireProgression(
            session.repoRoot().resolve(".piscine/progress.json"));
        Map<Integer, Map<String, List<ExerciseEntry>>> modules = new LinkedHashMap<>();
        boolean currentTrouve = false;
        StringBuilder sb = new StringBuilder("{\"modules\":[");
        for (SousGroupe sg : session.catalog().sousGroupes()) {
            for (ExerciseEntry e : sg.exercices()) {
                modules.computeIfAbsent(e.module(), k -> new LinkedHashMap<>())
                    .computeIfAbsent(sg.id(), k -> new ArrayList<>()).add(e);
            }
        }
        boolean firstModule = true;
        for (var module : modules.entrySet()) {
            if (!firstModule) sb.append(',');
            firstModule = false;
            sb.append("{\"module\":").append(module.getKey()).append(",\"sousGroupes\":[");
            boolean firstSg = true;
            for (var sg : module.getValue().entrySet()) {
                if (!firstSg) sb.append(',');
                firstSg = false;
                sb.append("{\"id\":\"").append(Json.escape(sg.getKey())).append("\",\"exos\":[");
                boolean firstExo = true;
                for (ExerciseEntry e : sg.getValue()) {
                    String status;
                    if (valides.contains(e.id())) {
                        status = "done";
                    } else if (!currentTrouve) {
                        status = "current";
                        currentTrouve = true;
                    } else {
                        status = "locked";
                    }
                    if (!firstExo) sb.append(',');
                    firstExo = false;
                    sb.append("{\"id\":\"").append(Json.escape(e.id()))
                      .append("\",\"slug\":\"").append(Json.escape(e.slug()))
                      .append("\",\"status\":\"").append(status).append("\"}");
                }
                sb.append("]}");
            }
            sb.append("]}");
        }
        sb.append("]}");
        respond(ex, 200, "application/json", sb.toString());
    }

    private Path reportsDir() {
        return session.repoRoot().resolve(".piscine/reports");
    }

    private void handleReports(HttpExchange ex) throws IOException {
        List<String> names = new ArrayList<>();
        Path dir = reportsDir();
        if (Files.isDirectory(dir)) {
            try (Stream<Path> files = Files.list(dir)) {
                files.filter(p -> p.getFileName().toString().endsWith(".md"))
                    .sorted(Comparator.comparing((Path p) -> p.getFileName().toString()).reversed())
                    .forEach(p -> names.add(p.getFileName().toString()));
            }
        }
        StringBuilder sb = new StringBuilder("{\"reports\":[");
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append('"').append(Json.escape(names.get(i))).append('"');
        }
        sb.append("]}");
        respond(ex, 200, "application/json", sb.toString());
    }

    private void handleReport(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();
        String name = (query != null && query.startsWith("name=")) ? query.substring(5) : null;
        if (name == null || !REPORT_NAME.matcher(name).matches()) {
            respond(ex, 404, "text/plain", "rapport introuvable");
            return;
        }
        Path file = reportsDir().resolve(name);
        if (!Files.isRegularFile(file)) { respond(ex, 404, "text/plain", "rapport introuvable"); return; }
        respond(ex, 200, "text/markdown; charset=utf-8", Files.readString(file));
    }

    /** Ouvre le workspace dans l'explorateur de fichiers ou un terminal (fire-and-forget). */
    private void handleOpen(HttpExchange ex) throws IOException {
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String target = Json.stringField(body, "target");
        String dir = session.repoRoot().toAbsolutePath().toString();
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        List<String> cmd;
        if ("explorer".equals(target)) {
            cmd = os.contains("win") ? List.of("explorer.exe", dir)
                : os.contains("mac") ? List.of("open", dir)
                : List.of("xdg-open", dir);
        } else if ("terminal".equals(target)) {
            cmd = os.contains("win")
                ? List.of("cmd", "/c", "start", "powershell", "-NoExit", "-Command", "Set-Location -LiteralPath '" + dir + "'")
                : os.contains("mac") ? List.of("open", "-a", "Terminal", dir)
                : List.of("x-terminal-emulator", "--working-directory=" + dir);
        } else {
            respond(ex, 400, "application/json", "{\"error\":\"target inconnu\"}");
            return;
        }
        try {
            new ProcessBuilder(cmd).start();
        } catch (Exception ignore) { /* best-effort : l'UI affiche le chemin de toute façon */ }
        respond(ex, 200, "application/json", "{\"ok\":true}");
    }

    /** Sert le site de cours statique sous /cours/ (index.html par défaut). */
    private void handleCours(HttpExchange ex) throws IOException {
        String rel = ex.getRequestURI().getPath().substring("/cours".length());
        if (rel.isEmpty() || rel.equals("/")) rel = "/index.html";
        Path root = siteDir.toAbsolutePath().normalize();
        Path file = root.resolve(rel.substring(1)).normalize();
        if (!file.startsWith(root)) { respond(ex, 404, "text/plain", "introuvable"); return; }
        if (Files.isDirectory(file)) file = file.resolve("index.html");
        if (!Files.isRegularFile(file)) { respond(ex, 404, "text/plain", "introuvable"); return; }
        byte[] bytes = Files.readAllBytes(file);
        ex.getResponseHeaders().set("Content-Type", contentType(file.getFileName().toString()));
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    private void handleStatic(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (path.equals("/")) path = "/index.html";
        if (path.contains("..")) { respond(ex, 404, "text/plain", "introuvable"); return; }
        try (InputStream in = GuiServer.class.getResourceAsStream("/web" + path)) {
            if (in == null) { respond(ex, 404, "text/plain", "introuvable : " + path); return; }
            byte[] bytes = in.readAllBytes();
            ex.getResponseHeaders().set("Content-Type", contentType(path));
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);
        } finally {
            ex.close();
        }
    }

    private static String contentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".js")) return "text/javascript; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".woff2")) return "font/woff2";
        return "application/octet-stream";
    }

    private static void respond(HttpExchange ex, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", contentType + (contentType.startsWith("text/plain") ? "; charset=utf-8" : ""));
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
}
