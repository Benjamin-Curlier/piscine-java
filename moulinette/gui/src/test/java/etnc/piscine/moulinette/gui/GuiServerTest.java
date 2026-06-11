package etnc.piscine.moulinette.gui;

import etnc.piscine.moulinette.console.ConsoleSession;
import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.GitClient;
import etnc.piscine.moulinette.console.git.GitResult;
import etnc.piscine.moulinette.console.git.RefUpdate;
import etnc.piscine.moulinette.console.repl.ReplContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuiServerTest {

    /** Git factice : toute commande réussit, branche fixe. */
    private static final class StubGit implements GitClient {
        @Override public GitResult run(Path repo, List<String> args) { return new GitResult(0, "", ""); }
        @Override public String currentBranch(Path repo) { return "main"; }
        @Override public List<RefUpdate> lastPushRefs(Path repo) { return List.of(); }
    }

    private GuiServer server;
    private HttpClient http;

    @BeforeEach
    void setUp() {
        var ctx = new ReplContext(Paths.get("."), new StubGit(), null, Mode.LOCAL);
        var session = ConsoleSession.of(ctx, CommandRegistry.defaults(null));
        server = GuiServer.start(session, 0);
        http = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        var req = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.port() + path))
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path) throws Exception {
        var req = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.port() + path)).build();
        return http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void terminal_execute_une_commande_et_rend_le_resultat_json() throws Exception {
        var r = post("/api/terminal", "{\"cmd\":\"help\"}");
        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.body()).contains("\"exitCode\":0").contains("Commandes supportées");
    }

    @Test
    void terminal_commande_inconnue_rend_exitcode_non_nul() throws Exception {
        var r = post("/api/terminal", "{\"cmd\":\"git checkout main\"}");
        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.body()).contains("\"exitCode\":1");
    }

    @Test
    void state_rend_la_branche_courante() throws Exception {
        var r = get("/api/state");
        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.body()).contains("\"branch\":\"main\"");
    }

    @Test
    void racine_sert_la_page_html() throws Exception {
        var r = get("/");
        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.body()).contains("<title>");
    }
}
