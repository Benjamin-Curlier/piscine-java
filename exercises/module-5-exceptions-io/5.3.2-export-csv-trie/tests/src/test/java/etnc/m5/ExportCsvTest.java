package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 5.3.2 (export CSV trié par ancienneté décroissante).
 */
@DisplayName("ExportCsv — tests publics")
class ExportCsvTest {

    @Test
    @DisplayName("3 personnels exportés : en-tête en première ligne puis 3 lignes triées ancienneté décroissante")
    void trois_personnels_tries_anciennete_decroissante(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("personnel.csv");
        // Intentionnellement dans un ordre mélangé pour vérifier le tri
        List<Personnel> personnels = List.of(
            new Personnel("Martin", Grade.SOLDAT, 2),
            new Personnel("Dupont", Grade.SERGENT, 10),
            new Personnel("Leroy", Grade.CAPORAL, 5)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(4);
        assertThat(lignes.get(0)).isEqualTo("nom,grade,anciennete");
        // Tri attendu : Dupont(10) > Leroy(5) > Martin(2)
        assertThat(lignes.get(1)).startsWith("Dupont,");
        assertThat(lignes.get(2)).startsWith("Leroy,");
        assertThat(lignes.get(3)).startsWith("Martin,");
    }

    @Test
    @DisplayName("format de ligne exact : nom,GRADE,anciennete")
    void format_ligne_exact(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("format.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Bernard", Grade.ADJUDANT, 8)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Le grade doit être le nom de la constante enum en majuscules
        assertThat(lignes.get(1)).isEqualTo("Bernard,ADJUDANT,8");
    }

    @Test
    @DisplayName("égalité d'ancienneté : départagée par nom alphabétique croissant")
    void egalite_anciennete_departage_par_nom(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("egalite.csv");
        // Tous les personnels ont la même ancienneté ; ordre alphabétique doit s'appliquer
        List<Personnel> personnels = List.of(
            new Personnel("Robert", Grade.SERGENT, 7),
            new Personnel("Andre", Grade.CAPORAL, 7),
            new Personnel("Michel", Grade.SOLDAT, 7)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(4);
        // Ordre alphabétique : Andre < Michel < Robert
        assertThat(lignes.get(1)).startsWith("Andre,");
        assertThat(lignes.get(2)).startsWith("Michel,");
        assertThat(lignes.get(3)).startsWith("Robert,");
    }

    @Test
    @DisplayName("liste vide : fichier avec uniquement l'en-tête")
    void liste_vide_produit_uniquement_entete(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("vide.csv");

        ExportCsv.exporter(List.of(), csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(1);
        assertThat(lignes.get(0)).isEqualTo("nom,grade,anciennete");
    }

    @Test
    @DisplayName("la liste source n'est pas mutée après l'export")
    void source_non_mutee(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("mutation.csv");
        // Ordre initial intentionnellement non trié par ancienneté
        List<Personnel> source = new ArrayList<>();
        source.add(new Personnel("Fontaine", Grade.SOLDAT, 1));
        source.add(new Personnel("Gauthier", Grade.SERGENT, 15));
        source.add(new Personnel("Petit", Grade.CAPORAL, 4));
        // Copie de l'ordre original pour vérification
        List<Personnel> ordreOriginal = new ArrayList<>(source);

        ExportCsv.exporter(source, csv);

        // La liste source doit conserver son ordre d'origine
        assertThat(source).containsExactlyElementsOf(ordreOriginal);
    }

    @Test
    @DisplayName("accents UTF-8 dans les noms : écriture et relecture correctes")
    void accents_utf8(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("accents.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Léonard", Grade.LIEUTENANT, 12),
            new Personnel("Éric", Grade.ADJUDANT, 9)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Léonard(12) avant Éric(9) : ancienneté décroissante
        assertThat(lignes.get(1)).startsWith("Léonard,");
        assertThat(lignes.get(2)).startsWith("Éric,");
    }

    @Test
    @DisplayName("grade().name() écrit le nom de la constante enum en majuscules")
    void grade_name_en_majuscules(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("grade.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Durand", Grade.LIEUTENANT, 20)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Le grade doit être "LIEUTENANT", pas "Lieutenant" ni "lieutenant"
        assertThat(lignes.get(1)).contains("LIEUTENANT");
    }

    @Test
    @DisplayName("en-tête toujours présent en première ligne, même avec plusieurs personnels")
    void entete_toujours_en_premiere_ligne(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("entete.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Simon", Grade.SERGENT, 6),
            new Personnel("Thomas", Grade.CAPORAL, 3)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes.get(0)).isEqualTo("nom,grade,anciennete");
        // Simon(6) puis Thomas(3)
        assertThat(lignes).hasSize(3);
    }
}
