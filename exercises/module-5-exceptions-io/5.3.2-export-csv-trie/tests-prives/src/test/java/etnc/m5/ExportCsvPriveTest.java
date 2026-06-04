package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 5.3.2 (cas limites et vérifications approfondies de l'export CSV).
 */
@DisplayName("ExportCsv — tests prives")
class ExportCsvPriveTest {

    @Test
    @DisplayName("un seul personnel : en-tête + exactement 1 ligne de données")
    void un_seul_personnel(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("un.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Renault", Grade.ADJUDANT, 14)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(2);
        assertThat(lignes.get(0)).isEqualTo("nom,grade,anciennete");
        assertThat(lignes.get(1)).isEqualTo("Renault,ADJUDANT,14");
    }

    @Test
    @DisplayName("4 personnels : ordre strict ancienneté décroissante vérifié sur chaque position")
    void quatre_elements_ordre_strict(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("quatre.csv");
        // Ordre délibérément inverse du tri attendu
        List<Personnel> personnels = List.of(
            new Personnel("Blanc", Grade.SOLDAT, 1),
            new Personnel("Chevalier", Grade.CAPORAL, 5),
            new Personnel("Moreau", Grade.SERGENT, 10),
            new Personnel("Vidal", Grade.LIEUTENANT, 20)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(5);
        assertThat(lignes.get(0)).isEqualTo("nom,grade,anciennete");
        // Ancienneté décroissante : 20, 10, 5, 1
        assertThat(lignes.get(1)).isEqualTo("Vidal,LIEUTENANT,20");
        assertThat(lignes.get(2)).isEqualTo("Moreau,SERGENT,10");
        assertThat(lignes.get(3)).isEqualTo("Chevalier,CAPORAL,5");
        assertThat(lignes.get(4)).isEqualTo("Blanc,SOLDAT,1");
    }

    @Test
    @DisplayName("écrasement d'un fichier existant : Files.write remplace le contenu précédent")
    void ecrasement_fichier_existant(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("ecrase.csv");
        // Premier export avec 2 personnels
        ExportCsv.exporter(List.of(
            new Personnel("Ancien1", Grade.SOLDAT, 3),
            new Personnel("Ancien2", Grade.CAPORAL, 6)
        ), csv);

        // Second export avec 1 seul personnel : doit écraser, pas appendre
        ExportCsv.exporter(List.of(
            new Personnel("Nouveau", Grade.SERGENT, 9)
        ), csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Le fichier doit contenir uniquement le second export (2 lignes : en-tête + 1 données)
        assertThat(lignes).hasSize(2);
        assertThat(lignes.get(1)).isEqualTo("Nouveau,SERGENT,9");
    }

    @Test
    @DisplayName("ancienneté négative : triée correctement (valeur numérique, décroissante)")
    void anciennete_negative_triee_correctement(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("negatif.csv");
        // Les anciennetés négatives sont rares mais légales ; le tri doit rester cohérent
        List<Personnel> personnels = List.of(
            new Personnel("Roux", Grade.SOLDAT, -2),
            new Personnel("Faure", Grade.CAPORAL, 0),
            new Personnel("Gros", Grade.SERGENT, 5)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Ordre décroissant : 5, 0, -2
        assertThat(lignes.get(1)).startsWith("Gros,");
        assertThat(lignes.get(2)).startsWith("Faure,");
        assertThat(lignes.get(3)).startsWith("Roux,");
    }

    @Test
    @DisplayName("deux mêmes anciennetés : tri secondaire par nom alphabétique croissant")
    void deux_memes_anciennetes_tri_par_nom(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("nom_alpha.csv");
        // Zoé et Alice ont la même ancienneté ; Alice doit venir avant Zoé
        List<Personnel> personnels = List.of(
            new Personnel("Zoé", Grade.SERGENT, 8),
            new Personnel("Alice", Grade.ADJUDANT, 8)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes.get(1)).startsWith("Alice,");
        assertThat(lignes.get(2)).startsWith("Zoé,");
    }

    @Test
    @DisplayName("round-trip : export puis relecture ligne par ligne cohérente avec les données source")
    void round_trip_export_relecture(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("roundtrip.csv");
        Personnel p1 = new Personnel("Leroux", Grade.LIEUTENANT, 18);
        Personnel p2 = new Personnel("Guillot", Grade.ADJUDANT, 12);
        Personnel p3 = new Personnel("Noël", Grade.CAPORAL, 3);

        ExportCsv.exporter(List.of(p1, p2, p3), csv);

        // Relecture manuelle (split CSV) pour vérifier la cohérence des données
        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Ligne 1 : Leroux(18), Ligne 2 : Guillot(12), Ligne 3 : Noël(3)
        String[] champs1 = lignes.get(1).split(",");
        assertThat(champs1[0]).isEqualTo("Leroux");
        assertThat(champs1[1]).isEqualTo("LIEUTENANT");
        assertThat(Integer.parseInt(champs1[2])).isEqualTo(18);

        String[] champs2 = lignes.get(2).split(",");
        assertThat(champs2[0]).isEqualTo("Guillot");
        assertThat(Integer.parseInt(champs2[2])).isEqualTo(12);

        // Noël avec accent doit être lu correctement en UTF-8
        String[] champs3 = lignes.get(3).split(",");
        assertThat(champs3[0]).isEqualTo("Noël");
        assertThat(Integer.parseInt(champs3[2])).isEqualTo(3);
    }

    @Test
    @DisplayName("ancienneté écrite comme entier sans décimales ni espaces")
    void anciennete_format_entier_strict(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("entier.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Picard", Grade.SERGENT, 7)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // "7" et non "7.0" ni " 7 " ni "07"
        assertThat(lignes.get(1)).isEqualTo("Picard,SERGENT,7");
    }

    @Test
    @DisplayName("export en UTF-8 : relecture avec StandardCharsets.UTF_8 produit les mêmes lignes")
    void fichier_ecrit_en_utf8(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("utf8.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Rémy", Grade.ADJUDANT, 11),
            new Personnel("Côté", Grade.LIEUTENANT, 15)
        );

        ExportCsv.exporter(personnels, csv);

        // La relecture explicitement en UTF-8 doit retrouver les accents intacts
        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Côté(15) avant Rémy(11) : ancienneté décroissante
        assertThat(lignes.get(1)).startsWith("Côté,");
        assertThat(lignes.get(2)).startsWith("Rémy,");
    }
}
