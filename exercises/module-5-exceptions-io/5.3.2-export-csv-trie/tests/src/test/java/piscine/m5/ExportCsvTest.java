package piscine.m5;

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
            new Personnel("Martin", Niveau.JUNIOR, 2),
            new Personnel("Dupont", Niveau.SENIOR, 10),
            new Personnel("Leroy", Niveau.CONFIRME, 5)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes).hasSize(4);
        assertThat(lignes.get(0)).isEqualTo("nom,niveau,anciennete");
        // Tri attendu : Dupont(10) > Leroy(5) > Martin(2)
        assertThat(lignes.get(1)).startsWith("Dupont,");
        assertThat(lignes.get(2)).startsWith("Leroy,");
        assertThat(lignes.get(3)).startsWith("Martin,");
    }

    @Test
    @DisplayName("format de ligne exact : nom,NIVEAU,anciennete")
    void format_ligne_exact(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("format.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Bernard", Niveau.LEAD, 8)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Le niveau doit être le nom de la constante enum en majuscules
        assertThat(lignes.get(1)).isEqualTo("Bernard,LEAD,8");
    }

    @Test
    @DisplayName("égalité d'ancienneté : départagée par nom alphabétique croissant")
    void egalite_anciennete_departage_par_nom(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("egalite.csv");
        // Tous les personnels ont la même ancienneté ; ordre alphabétique doit s'appliquer
        List<Personnel> personnels = List.of(
            new Personnel("Robert", Niveau.SENIOR, 7),
            new Personnel("Andre", Niveau.CONFIRME, 7),
            new Personnel("Michel", Niveau.JUNIOR, 7)
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
        assertThat(lignes.get(0)).isEqualTo("nom,niveau,anciennete");
    }

    @Test
    @DisplayName("la liste source n'est pas mutée après l'export")
    void source_non_mutee(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("mutation.csv");
        // Ordre initial intentionnellement non trié par ancienneté
        List<Personnel> source = new ArrayList<>();
        source.add(new Personnel("Fontaine", Niveau.JUNIOR, 1));
        source.add(new Personnel("Gauthier", Niveau.SENIOR, 15));
        source.add(new Personnel("Petit", Niveau.CONFIRME, 4));
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
            new Personnel("Léonard", Niveau.PRINCIPAL, 12),
            new Personnel("Éric", Niveau.LEAD, 9)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Léonard(12) avant Éric(9) : ancienneté décroissante
        assertThat(lignes.get(1)).startsWith("Léonard,");
        assertThat(lignes.get(2)).startsWith("Éric,");
    }

    @Test
    @DisplayName("niveau().name() écrit le nom de la constante enum en majuscules")
    void niveau_name_en_majuscules(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("niveau.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Durand", Niveau.PRINCIPAL, 20)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        // Le niveau doit être "PRINCIPAL", pas "Principal" ni "principal"
        assertThat(lignes.get(1)).contains("PRINCIPAL");
    }

    @Test
    @DisplayName("en-tête toujours présent en première ligne, même avec plusieurs personnels")
    void entete_toujours_en_premiere_ligne(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("entete.csv");
        List<Personnel> personnels = List.of(
            new Personnel("Simon", Niveau.SENIOR, 6),
            new Personnel("Thomas", Niveau.CONFIRME, 3)
        );

        ExportCsv.exporter(personnels, csv);

        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);
        assertThat(lignes.get(0)).isEqualTo("nom,niveau,anciennete");
        // Simon(6) puis Thomas(3)
        assertThat(lignes).hasSize(3);
    }
}
