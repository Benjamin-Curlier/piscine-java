package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests privés de l'exercice 5.3.1 (cas limites et chaînage des causes).
 */
@DisplayName("ImportCsv — tests prives")
class ImportCsvPriveTest {

    @Test
    @DisplayName("grade inconnu : la cause chaînée est l'IllegalArgumentException de Grade.valueOf")
    void grade_inconnu_cause_est_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,COLONEL,5\n",
                StandardCharsets.UTF_8);

        // La cause d'origine vient de Grade.valueOf, qui lève IllegalArgumentException.
        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .getCause()
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("ancienneté non numérique : la cause chaînée est une NumberFormatException")
    void anciennete_non_numerique_cause_est_nfe(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT,douze\n",
                StandardCharsets.UTF_8);

        // La cause d'origine vient de Integer.parseInt, qui lève NumberFormatException.
        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .getCause()
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("les champs entourés d'espaces sont nettoyés (trim)")
    void champs_avec_espaces_sont_trimes(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Espaces autour de chaque champ — doivent être supprimés
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + " Dupont , SERGENT , 5 \n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Dupont", Grade.SERGENT, 5));
    }

    @Test
    @DisplayName("nom avec accents UTF-8 : lu correctement")
    void nom_accents_utf8(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Légère,ADJUDANT,8\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Légère", Grade.ADJUDANT, 8));
    }

    @Test
    @DisplayName("l'ordre des lignes du fichier est préservé")
    void ordre_des_lignes_preserve(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Zoé,SOLDAT,1\n"
                        + "Alpha,SOLDAT,1\n"
                        + "Mike,SOLDAT,1\n",
                StandardCharsets.UTF_8);

        // L'import ne trie pas : l'ordre du fichier doit être conservé tel quel.
        assertThat(ImportCsv.importer(csv))
                .extracting(Personnel::nom)
                .containsExactly("Zoé", "Alpha", "Mike");
    }

    @Test
    @DisplayName("une seule ligne de données : liste à un élément")
    void une_seule_ligne_de_donnees(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Solo,CAPORAL,3\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Solo", Grade.CAPORAL, 3));
    }

    @Test
    @DisplayName("ligne à 4 champs : IllegalArgumentException")
    void ligne_a_quatre_champs_leve_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Un champ en trop : 4 champs au lieu de 3
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT,5,extra\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dupont,SERGENT,5,extra");
    }

    @Test
    @DisplayName("ancienneté négative : acceptée (on lit ce qui est écrit)")
    void anciennete_negative_acceptee(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // -2 est un entier valide : aucun contrôle métier ici, on le lit tel quel.
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT,-2\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Dupont", Grade.SERGENT, -2));
    }
}
