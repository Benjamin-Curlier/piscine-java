package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 5.2.2 (cas limites — copie octet-exacte).
 */
@DisplayName("CopieFichier — tests prives")
class CopieFichierPriveTest {

    @Test
    @DisplayName("octet-exact : source sans antislash-n final → destination sans antislash-n final")
    void source_sans_saut_final_destination_sans_saut_final(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // Pas de \n final : la copie ne doit pas en ajouter
        Files.writeString(source, "sans saut de ligne final", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("sans saut de ligne final");
    }

    @Test
    @DisplayName("modifier la source apres copie n'affecte pas la destination")
    void modifier_source_apres_copie_sans_effet(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(source, "contenu initial\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        // Modification de la source APRES la copie
        Files.writeString(source, "contenu modifie\n", StandardCharsets.UTF_8);

        // La destination doit garder le contenu au moment de la copie
        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("contenu initial\n");
    }

    @Test
    @DisplayName("copie d'un fichier a une seule ligne sans saut final")
    void fichier_une_ligne_sans_saut(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(source, "une seule ligne", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("une seule ligne");
    }

    @Test
    @DisplayName("recopie successive (idempotence) : deux copies successives donnent le meme contenu")
    void recopie_successive_idempotente(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.writeString(source, "alpha\nbravo\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);
        CopieFichier.copier(source, destination); // deuxieme copie sur la meme destination

        // Le contenu doit rester celui de source, pas être corrompu
        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("alpha\nbravo\n");
    }

    @Test
    @DisplayName("contenu avec uniquement des espaces est preserve octet-exact")
    void contenu_espaces_preserve(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // Contenu avec espaces et tabulations — aucun trim ne doit avoir lieu
        Files.writeString(source, "  \t  \n   \n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("  \t  \n   \n");
    }

    @Test
    @DisplayName("ecrasement : l'ancien contenu plus long n'est pas conserve en queue")
    void ecrasement_supprime_ancien_contenu_long(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // Destination initialement plus longue que la source
        Files.writeString(destination, "ligne1\nligne2\nligne3\nligne4\n", StandardCharsets.UTF_8);
        Files.writeString(source, "court\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        // La destination ne doit contenir QUE le contenu de source
        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("court\n");
    }

    @Test
    @DisplayName("contenu binaire (octets non-UTF-8) est copie sans corruption")
    void contenu_bytes_preserve(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.bin");
        Path destination = tempDir.resolve("destination.bin");
        // Quelques octets dont certains hors ASCII pour vérifier la copie binaire
        byte[] octets = new byte[]{0x00, 0x01, (byte) 0xFF, (byte) 0xFE, 0x41, 0x42};
        Files.write(source, octets);

        CopieFichier.copier(source, destination);

        assertThat(Files.readAllBytes(destination)).isEqualTo(octets);
    }

    @Test
    @DisplayName("source avec plusieurs sauts de ligne consecutifs est copiee fidelement")
    void sauts_consecutifs_preserves(@TempDir Path tempDir) throws Exception {
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        // Lignes vides entre les messages — représente un journal d'événements avec séparateurs
        Files.writeString(source, "message 1\n\n\nmessage 2\n\n", StandardCharsets.UTF_8);

        CopieFichier.copier(source, destination);

        assertThat(Files.readString(destination, StandardCharsets.UTF_8))
            .isEqualTo("message 1\n\n\nmessage 2\n\n");
    }
}
