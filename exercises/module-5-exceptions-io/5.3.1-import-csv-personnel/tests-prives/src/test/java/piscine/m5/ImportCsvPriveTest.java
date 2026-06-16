package piscine.m5;

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
    @DisplayName("niveau inconnu : la cause chaînée est l'IllegalArgumentException de Niveau.valueOf")
    void niveau_inconnu_cause_est_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,niveau,anciennete\n"
                        + "Dupont,DIRECTEUR,5\n",
                StandardCharsets.UTF_8);

        // La cause d'origine vient de Niveau.valueOf, qui lève IllegalArgumentException.
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
                "nom,niveau,anciennete\n"
                        + "Dupont,SENIOR,douze\n",
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
                "nom,niveau,anciennete\n"
                        + " Dupont , SENIOR , 5 \n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Dupont", Niveau.SENIOR, 5));
    }

    @Test
    @DisplayName("nom avec accents UTF-8 : lu correctement")
    void nom_accents_utf8(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,niveau,anciennete\n"
                        + "Légère,LEAD,8\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Légère", Niveau.LEAD, 8));
    }

    @Test
    @DisplayName("l'ordre des lignes du fichier est préservé")
    void ordre_des_lignes_preserve(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        Files.writeString(csv,
                "nom,niveau,anciennete\n"
                        + "Zoé,JUNIOR,1\n"
                        + "Alpha,JUNIOR,1\n"
                        + "Mike,JUNIOR,1\n",
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
                "nom,niveau,anciennete\n"
                        + "Solo,CONFIRME,3\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Solo", Niveau.CONFIRME, 3));
    }

    @Test
    @DisplayName("ligne à 4 champs : IllegalArgumentException")
    void ligne_a_quatre_champs_leve_iae(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Un champ en trop : 4 champs au lieu de 3
        Files.writeString(csv,
                "nom,niveau,anciennete\n"
                        + "Dupont,SENIOR,5,extra\n",
                StandardCharsets.UTF_8);

        assertThatThrownBy(() -> ImportCsv.importer(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dupont,SENIOR,5,extra");
    }

    @Test
    @DisplayName("ancienneté négative : acceptée (on lit ce qui est écrit)")
    void anciennete_negative_acceptee(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // -2 est un entier valide : aucun contrôle métier ici, on le lit tel quel.
        Files.writeString(csv,
                "nom,niveau,anciennete\n"
                        + "Dupont,SENIOR,-2\n",
                StandardCharsets.UTF_8);

        assertThat(ImportCsv.importer(csv))
                .containsExactly(new Personnel("Dupont", Niveau.SENIOR, -2));
    }
}
