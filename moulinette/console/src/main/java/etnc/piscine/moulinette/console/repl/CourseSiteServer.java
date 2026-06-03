package etnc.piscine.moulinette.console.repl;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Path;

/** Sert un dossier statique (le site Docusaurus) sur 127.0.0.1, en in-process. */
public final class CourseSiteServer implements AutoCloseable {

    private final HttpServer server;
    private final URI url;

    private CourseSiteServer(HttpServer server, URI url) {
        this.server = server;
        this.url = url;
    }

    /**
     * Démarre le serveur en essayant les ports de {@code preferredPort} à {@code maxPort} inclus.
     *
     * @throws IllegalStateException si aucun port n'est libre dans l'intervalle.
     */
    public static CourseSiteServer start(Path siteDir, int preferredPort, int maxPort) {
        Path root = siteDir.toAbsolutePath().normalize();
        UncheckedIOException last = null;
        for (int port = preferredPort; port <= maxPort; port++) {
            try {
                InetSocketAddress addr =
                    new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
                HttpServer s = SimpleFileServer.createFileServer(
                    addr, root, SimpleFileServer.OutputLevel.NONE);
                s.start();
                return new CourseSiteServer(s, URI.create("http://127.0.0.1:" + port + "/"));
            } catch (UncheckedIOException e) {
                last = e; // port probablement occupé : on tente le suivant
            }
        }
        throw new IllegalStateException(
            "Aucun port libre entre " + preferredPort + " et " + maxPort, last);
    }

    public URI url() {
        return url;
    }

    public void stop() {
        server.stop(0);
    }

    @Override
    public void close() {
        stop();
    }
}
