package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 5.2.1 (cas limites du comptage de lignes).
 */
@DisplayName("CompteurLignes — tests prives")
class CompteurLignesPriveTest {

    @Test
    @DisplayName("fichier uniquement blanc : compterLignesNonVides renvoie 0")
    void fichier_uniquement_blanc_non_vides_zero(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("blancs.txt");
        // Trois lignes blanches — aucune ne passe le filtre isBlank()
        Files.writeString(fichier, "   \n\t\n  \t  \n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(0);
        // Le total, lui, doit quand même refléter les 3 lignes présentes
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(3);
    }

    @Test
    @DisplayName("presence ou absence du \\n final donne le meme total")
    void saut_de_ligne_final_neutre(@TempDir Path tempDir) throws Exception {
        Path avecFinal = tempDir.resolve("avec.txt");
        Path sansFinal = tempDir.resolve("sans.txt");
        // Deux lignes avec \n final
        Files.writeString(avecFinal, "delta\nepsilon\n", StandardCharsets.UTF_8);
        // Deux lignes sans \n final — Files.lines compte quand meme les deux
        Files.writeString(sansFinal, "delta\nepsilon", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(avecFinal)).isEqualTo(2);
        assertThat(CompteurLignes.compterLignes(sansFinal)).isEqualTo(2);
    }

    @Test
    @DisplayName("gros fichier de 50 lignes : compterLignes renvoie 50")
    void gros_fichier_50_lignes(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("gros.txt");
        // Construction d'un fichier de 50 lignes numerotees
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 50; i++) {
            sb.append("ligne-").append(i).append("\n");
        }
        Files.writeString(fichier, sb.toString(), StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(50);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(50);
    }

    @Test
    @DisplayName("une seule ligne vide \\n : total 1, non-vides 0")
    void une_seule_ligne_vide(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("seule-vide.txt");
        // "\n" = une ligne vide terminée par un saut de ligne
        Files.writeString(fichier, "\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(1);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(0);
    }

    @Test
    @DisplayName("melange lignes remplies et vides : decomptes independants corrects")
    void melange_rempli_et_vide(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("melange.txt");
        // 5 lignes : 3 remplies, 2 vides
        Files.writeString(fichier, "foxtrot\n\ngolf\n\nhotel\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(5);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(3);
    }

    @Test
    @DisplayName("ligne unique sans \\n final : compterLignesNonVides renvoie 1 si non blanche")
    void ligne_unique_sans_saut_non_vide(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("unique-contenu.txt");
        Files.writeString(fichier, "india", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(1);
    }

    @Test
    @DisplayName("ligne unique blanche sans \\n final : compterLignesNonVides renvoie 0")
    void ligne_unique_blanche_sans_saut(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("unique-blanche.txt");
        // Ligne blanche sans \n final — isBlank() renvoie true, donc exclue
        Files.writeString(fichier, "   ", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(1);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(0);
    }

    @Test
    @DisplayName("caracteres accentues et speciaux UTF-8 dans une ligne non vide : comptes")
    void accents_et_speciaux_utf8(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("utf8-riche.txt");
        // Caracteres accentues, cedille, tréma — doivent etre lus correctement en UTF-8
        Files.writeString(fichier, "général\nétançon\nnaïveté\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(3);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(3);
    }
}
