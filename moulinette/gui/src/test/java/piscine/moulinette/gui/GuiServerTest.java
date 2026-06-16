package piscine.moulinette.gui;

import piscine.moulinette.console.ConsoleSession;
import piscine.moulinette.console.Mode;
import piscine.moulinette.console.commands.CommandRegistry;
import piscine.moulinette.console.git.GitClient;
import piscine.moulinette.console.git.GitResult;
import piscine.moulinette.console.git.RefUpdate;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.workspace.ExerciseCatalog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
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

    @Test
    void open_cible_inconnue_rend_400() throws Exception {
        var r = post("/api/open", "{\"target\":\"calculatrice\"}");
        assertThat(r.statusCode()).isEqualTo(400);
    }

    /** Serveur câblé sur un vrai mini-workspace : catalogue 2 exos, 1 validé, 1 rapport. */
    private GuiServer serverAvecWorkspace(Path tmp) throws Exception {
        Path exos = tmp.resolve("piscine/exercises");
        ecrireExo(exos, "9.9.1-alpha", "alpha", 1);
        ecrireExo(exos, "9.9.2-beta", "beta", 2);
        Path repo = tmp.resolve("repo");
        Files.createDirectories(repo.resolve(".piscine/reports"));
        Files.writeString(repo.resolve(".piscine/progress.json"), "{\n  \"9.9.1\": true\n}\n");
        Files.writeString(repo.resolve(".piscine/reports/9.9-2026-01-01-0900.md"),
            "# Rapport moulinette — sous-groupe 9.9\n");
        var ctx = new ReplContext(repo, new StubGit(), ExerciseCatalog.scan(exos), Mode.LOCAL);
        return GuiServer.start(ConsoleSession.of(ctx, CommandRegistry.defaults(null)), 0);
    }

    private static void ecrireExo(Path exosRoot, String dir, String slug, int position) throws Exception {
        Path exo = exosRoot.resolve(dir);
        Files.createDirectories(exo);
        Files.writeString(exo.resolve("metadata.yml"),
            "slug: " + slug + "\nmodule: 9\nsous_groupe: \"9.9\"\nposition: " + position + "\nnotions: []\n");
    }

    @Test
    void progress_rend_les_statuts_sequentiels(@TempDir Path tmp) throws Exception {
        GuiServer s = serverAvecWorkspace(tmp);
        try {
            var r = http.send(HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + s.port() + "/api/progress")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(r.statusCode()).isEqualTo(200);
            assertThat(r.body())
                .contains("\"id\":\"9.9.1\"").contains("\"status\":\"done\"")
                .contains("\"id\":\"9.9.2\"").contains("\"status\":\"current\"");
        } finally {
            s.stop();
        }
    }

    @Test
    void reports_liste_et_sert_les_rapports(@TempDir Path tmp) throws Exception {
        GuiServer s = serverAvecWorkspace(tmp);
        try {
            var list = http.send(HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + s.port() + "/api/reports")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(list.body()).contains("9.9-2026-01-01-0900.md");

            var one = http.send(HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + s.port() + "/api/report?name=9.9-2026-01-01-0900.md")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(one.statusCode()).isEqualTo(200);
            assertThat(one.body()).contains("Rapport moulinette");

            var bad = http.send(HttpRequest.newBuilder(
                    URI.create("http://127.0.0.1:" + s.port() + "/api/report?name=../../secret.md")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(bad.statusCode()).isEqualTo(404);
        } finally {
            s.stop();
        }
    }
}
