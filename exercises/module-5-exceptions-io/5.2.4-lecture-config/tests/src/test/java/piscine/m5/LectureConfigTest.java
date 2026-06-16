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
 * Tests publics de l'exercice 5.2.4 (lecture d'un fichier de configuration cle=valeur).
 *
 * Chaque test écrit son fichier dans le dossier temporaire fourni par JUnit
 * ({@code @TempDir}) : aucun fichier hors sandbox, nettoyage automatique.
 * UTF-8 et fins de ligne {@code \n} sont explicites.
 */
@DisplayName("LectureConfig — tests publics")
class LectureConfigTest {

    @Test
    @DisplayName("lireValeur renvoie la valeur d'une cle presente (langue=fr)")
    void lireValeur_cle_presente_langue(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\nniveau=3\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireValeur(config, "langue", "??")).isEqualTo("fr");
    }

    @Test
    @DisplayName("lireValeur renvoie la valeur d'une seconde cle (niveau=3)")
    void lireValeur_cle_presente_niveau(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\nniveau=3\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireValeur(config, "niveau", "0")).isEqualTo("3");
    }

    @Test
    @DisplayName("lireValeur renvoie la valeur par defaut si la cle est absente")
    void lireValeur_cle_absente_defaut(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\nniveau=3\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireValeur(config, "theme", "clair")).isEqualTo("clair");
    }

    @Test
    @DisplayName("lireConfig renvoie la table des deux paires")
    void lireConfig_deux_paires(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\nniveau=3\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsEntry("langue", "fr")
            .containsEntry("niveau", "3")
            .hasSize(2);
    }

    @Test
    @DisplayName("une ligne de commentaire (# titre) est ignoree")
    void ligne_commentaire_ignoree(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "# titre\nlangue=fr\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config))
            .containsExactly(Map.entry("langue", "fr"));
    }

    @Test
    @DisplayName("une ligne blanche est ignoree")
    void ligne_blanche_ignoree(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "langue=fr\n\nniveau=3\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config)).hasSize(2);
    }

    @Test
    @DisplayName("les espaces autour de la cle et de la valeur sont rognes")
    void espaces_trimes(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "  a = b  \n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config)).containsEntry("a", "b");
    }

    @Test
    @DisplayName("une valeur vide (cle=) donne une chaine vide")
    void valeur_vide(@TempDir Path tempDir) throws Exception {
        Path config = tempDir.resolve("app.conf");
        Files.writeString(config, "cle=\n", StandardCharsets.UTF_8);

        assertThat(LectureConfig.lireConfig(config)).containsEntry("cle", "");
    }
}
