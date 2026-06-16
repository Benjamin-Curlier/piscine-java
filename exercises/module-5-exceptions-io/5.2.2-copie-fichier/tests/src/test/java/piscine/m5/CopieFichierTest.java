package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 5.2.2 (copie de fichier octet-exact).
 */
@DisplayName("CopieFichier — tests publics")
class CopieFichierTest {

    @Test
    @DisplayName("le contenu de destination est identique a celui de source apres copie")
    void contenu_destination_egal_source(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(source, "Bonjour\nMonde\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("Bonjour\nMonde\n");
    }

    @Test
    @DisplayName("les accents et caracteres UTF-8 sont preserves apres copie")
    void accents_utf8_preserves(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // Domaine : notes d'équipe avec accents
        Files.writeString(source, "Équipe\nDéploiement\nIntégré\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("Équipe\nDéploiement\nIntégré\n");
    }

    @Test
    @DisplayName("la source est inchangee apres la copie")
    void source_inchangee_apres_copie(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(source, "contenu original\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(source, StandardCharsets.UTF_8))
            .isEqualTo("contenu original\n");
    }

    @Test
    @DisplayName("copier un fichier vide produit une destination vide")
    void fichier_vide_destination_vide(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("vide.txt");
        Path destination = tempDir.resolve("copie-vide.txt");
        Files.writeString(source, "", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8)).isEqualTo("");
    }

    @Test
    @DisplayName("un fichier multi-lignes avec antislash-n est copie fidelement")
    void multi_lignes_preservees(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("multilignes.txt");
        Path destination = tempDir.resolve("copie-multilignes.txt");
        String contenu = "alpha\nbravo\ncharlie\ndelta\n";
        Files.writeString(source, contenu, StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo(contenu);
    }

    @Test
    @DisplayName("ecraser une destination existante (REPLACE_EXISTING) — pas d'exception")
    void ecrasement_destination_existante(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // La destination existe déjà avec un autre contenu
        Files.writeString(destination, "ancien contenu\n", StandardCharsets.UTF_8);
        Files.writeString(source, "nouveau contenu\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        // La destination doit prendre le contenu de source, pas l'ancien contenu
        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("nouveau contenu\n");
    }

    @Test
    @DisplayName("apres ecrasement le contenu de destination est celui de source")
    void ecrasement_contenu_correct(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(destination, "a\nb\nc\n", StandardCharsets.UTF_8);
        Files.writeString(source, "x\ny\nz\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("x\ny\nz\n");
    }

    @Test
    @DisplayName("la destination est creee si elle n'existe pas")
    void destination_creee_si_absente(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("nouveau.txt");
        Files.writeString(source, "tache accomplie\n", StandardCharsets.UTF_8);
        // destination n'existe pas encore

        CopieFichier.copier(source, destination);

        assertThat(Files.exists(destination)).isTrue();
        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("tache accomplie\n");
    }
}
