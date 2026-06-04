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
 * Tests publics de l'exercice 5.3.1 (import CSV → List&lt;Personnel&gt;).
 */
@DisplayName("ImportCsv — tests publics")
class ImportCsvTest {

    @Test
    @DisplayName("CSV valide de 3 lignes : renvoie exactement les 3 Personnel")
    void csv_valide_trois_lignes(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // En-tête + 3 lignes de données, \n explicite pour la portabilité
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Durand,SERGENT,5\n"
                        + "Martin,CAPORAL,2\n"
                        + "Petit,LIEUTENANT,12\n",
                StandardCharsets.UTF_8);

        List<Personnel> personnels = ImportCsv.importer(csv);

        assertThat(personnels).containsExactly(
                new Personnel("Durand", Grade.SERGENT, 5),
                new Personnel("Martin", Grade.CAPORAL, 2),
                new Personnel("Petit", Grade.LIEUTENANT, 12));
    }

    @Test
    @DisplayName("l'en-tête est sauté : aucun Personnel nommé « nom »")
    void en_tete_saute(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Durand,SERGENT,5\n",
                StandardCharsets.UTF_8);

        List<Personnel> personnels = ImportCsv.importer(csv);

        // L'en-tête ne doit pas être transformé en Personnel
        assertThat(personnels).hasSize(1);
        assertThat(personnels).extracting(Personnel::nom).doesNotContain("nom");
    }

    @Test
    @DisplayName("les lignes blanches sont ignorées")
    void lignes_blanches_ignorees(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Lignes vides et lignes d'espaces intercalées entre les données
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Durand,SERGENT,5\n"
                        + "\n"
                        + "   \n"
                        + "Martin,CAPORAL,2\n",
                StandardCharsets.UTF_8);

        List<Personnel> personnels = ImportCsv.importer(csv);

        assertThat(personnels).containsExactly(
                new Personnel("Durand", Grade.SERGENT, 5),
                new Personnel("Martin", Grade.CAPORAL, 2));
    }

    @Test
    @DisplayName("fichier ne contenant que l'en-tête : liste vide")
    void en_tete_seul_liste_vide(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv, "nom,grade,anciennete\n", StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv)).isEmpty();
    }

    @Test
    @DisplayName("grade inconnu : IllegalArgumentException dont le message cite le grade fautif")
    void grade_inconnu_leve_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,GENERAL,5\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GENERAL");
    }

    @Test
    @DisplayName("ancienneté non numérique : IllegalArgumentException dont le message cite la valeur")
    void anciennete_non_numerique_leve_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT,abc\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("abc");
    }

    @Test
    @DisplayName("nombre de champs différent de 3 : IllegalArgumentException")
    void mauvais_nombre_de_champs_leve_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Deux champs seulement (ancienneté manquante)
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("le message d'erreur de ligne invalide contient la ligne fautive")
    void message_contient_ligne_fautive(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,grade,anciennete\n"
                        + "Dupont,SERGENT\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dupont,SERGENT");
    }
}
