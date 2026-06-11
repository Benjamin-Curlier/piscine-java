package etnc.piscine.moulinette.gui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import etnc.piscine.moulinette.console.ConsoleSession;
import etnc.piscine.moulinette.console.commands.CommandResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Serveur HTTP local de la GUI : expose une {@link ConsoleSession} en JSON et sert
 * le frontend statique embarqué (classpath {@code /web}). Lié à 127.0.0.1 uniquement.
 */
public final class GuiServer {

    private final HttpServer server;
    private final ConsoleSession session;

    private GuiServer(HttpServer server, ConsoleSession session) {
        this.server = server;
        this.session = session;
    }

    /** Démarre sur {@code port} (0 = port éphémère libre). */
    public static GuiServer start(ConsoleSession session, int port) {
        try {
            var addr = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
            HttpServer s = HttpServer.create(addr, 0);
            GuiServer gui = new GuiServer(s, session);
            s.createContext("/", gui::handleStatic);
            s.createContext("/api/terminal", gui::handleTerminal);
            s.createContext("/api/state", gui::handleState);
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
                + "\",\"repoRoot\":\"" + Json.escape(session.repoRoot().toString()) + "\"}");
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
