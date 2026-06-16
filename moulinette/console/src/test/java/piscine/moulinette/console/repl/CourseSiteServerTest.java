package piscine.moulinette.console.repl;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class CourseSiteServerTest {

    @Test
    void sert_index_html_sur_le_port_prefere() throws Exception {
        Path site = Files.createTempDirectory("site");
        Files.writeString(site.resolve("index.html"), "<h1>Piscine</h1>");

        try (CourseSiteServer server = CourseSiteServer.start(site, 8830, 8840)) {
            assertThat(server.url().toString()).isEqualTo("http://127.0.0.1:8830/");
            HttpResponse<String> resp = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(server.url() + "index.html")).build(),
                HttpResponse.BodyHandlers.ofString());
            assertThat(resp.statusCode()).isEqualTo(200);
            assertThat(resp.body()).contains("Piscine");
        }
    }

    @Test
    void bascule_sur_le_port_suivant_si_le_prefere_est_pris() throws Exception {
        Path site = Files.createTempDirectory("site");
        Files.writeString(site.resolve("index.html"), "ok");

        try (CourseSiteServer first = CourseSiteServer.start(site, 8841, 8850);
             CourseSiteServer second = CourseSiteServer.start(site, 8841, 8850)) {
            assertThat(first.url().toString()).isEqualTo("http://127.0.0.1:8841/");
            assertThat(second.url().toString()).isEqualTo("http://127.0.0.1:8842/");
        }
    }
}
