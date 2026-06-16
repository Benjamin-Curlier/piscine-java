package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 5.2.4 (cas limites du parsing cle=valeur).
 *
 * Chaque test écrit son fichier dans le {@code @TempDir} fourni par JUnit.
 * UTF-8 et fins de ligne {@code \n} explicites.
 */
@DisplayName("LectureConfig — tests prives")
class LectureConfigPriveTest {

    @Test
    @DisplayName("la valeur peut contenir des '=' : coupe au PREMIER '='")
    void egal_dans_la_valeur(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "url=http://x?a=b\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsEntry("url", "http://x?a=b");
    }

    @Test
    @DisplayName("cle dupliquee : la derniere occurrence l'emporte")
    void cle_dupliquee_derniere_gagne(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\nlangue=en\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsEntry("langue", "en")
            .hasSize(1);
    }

    @Test
    @DisplayName("une ligne sans '=' est ignoree")
    void ligne_sans_egal_ignoree(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "ceci_n_est_pas_une_paire\nlangue=fr\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsExactly(Map.entry("langue", "fr"));
    }

    @Test
    @DisplayName("les cles sont sensibles a la casse (Langue != langue)")
    void cles_sensibles_a_la_casse(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "Langue=FR\nlangue=fr\n", StandardCharsets.UTF_8);

        Map<String, String> resultat = LectureConfig.lireConfig(config);
        assertThat(resultat).hasSize(2);
        assertThat(resultat).containsEntry("Langue", "FR");
        assertThat(resultat).containsEntry("langue", "fr");
    }

    @Test
    @DisplayName("lireValeur delegue bien a lireConfig (commentaire ignore, defaut applique)")
    void lireValeur_delegue_au_parsing(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "# commentaire\nniveau=5\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireValeur(config, "niveau", "0")).isEqualTo("5");
        // La clé "# commentaire" ne doit jamais être une entrée : on récupère le défaut.
        assertThat(LectureConfig.lireValeur(config, "commentaire", "absent")).isEqualTo("absent");
    }

    @Test
    @DisplayName("fichier vide : lireConfig renvoie une table vide et lireValeur le defaut")
    void fichier_vide(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config)).isEmpty();
        assertThat(LectureConfig.lireValeur(config, "langue", "fr")).isEqualTo("fr");
    }

    @Test
    @DisplayName("melange de commentaires, lignes blanches et paires")
    void melange_commentaires_blancs_paires(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config,
            "# Configuration de l'application\n"
            + "\n"
            + "langue = fr\n"
            + "  # niveau de log\n"
            + "niveau=3\n"
            + "\n",
            StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsEntry("langue", "fr")
            .containsEntry("niveau", "3")
            .hasSize(2);
    }

    @Test
    @DisplayName("valeur accentuee (UTF-8) preservee a la lecture")
    void valeur_accentuee_utf8(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "ville=Châteauroux\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireValeur(config, "ville", "?")).isEqualTo("Châteauroux");
    }
}
