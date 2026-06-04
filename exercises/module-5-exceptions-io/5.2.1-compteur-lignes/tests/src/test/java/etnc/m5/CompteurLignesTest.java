package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 5.2.1 (comptage de lignes via Files.lines).
 */
@DisplayName("CompteurLignes — tests publics")
class CompteurLignesTest {

    @Test
    @DisplayName("fichier de 3 lignes remplies : compterLignes renvoie 3")
    void trois_lignes_remplies_total(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("donnees.txt");
        // Trois lignes de contenu, chacune terminée par \n explicite (portabilité)
        Files.writeString(fichier, "alpha\nbravo\ncharlie\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(3);
    }

    @Test
    @DisplayName("fichier de 3 lignes remplies : compterLignesNonVides renvoie 3")
    void trois_lignes_remplies_non_vides(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("donnees.txt");
        Files.writeString(fichier, "alpha\nbravo\ncharlie\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(3);
    }

    @Test
    @DisplayName("fichier vide : compterLignes renvoie 0")
    void fichier_vide_total(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("vide.txt");
        Files.writeString(fichier, "", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(0);
    }

    @Test
    @DisplayName("fichier vide : compterLignesNonVides renvoie 0")
    void fichier_vide_non_vides(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("vide.txt");
        Files.writeString(fichier, "", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(0);
    }

    @Test
    @DisplayName("lignes vides intercalees : compterLignes renvoie 6, compterLignesNonVides renvoie 3")
    void lignes_vides_intercalees(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("intercale.txt");
        // "alpha\n\nbravo\n\n\ncharlie\n" = 6 lignes dont 3 vides
        Files.writeString(fichier, "alpha\n\nbravo\n\n\ncharlie\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(6);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(3);
    }

    @Test
    @DisplayName("ligne unique sans saut de ligne final : compterLignes renvoie 1")
    void ligne_unique_sans_retour_chariot(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("unique.txt");
        // Pas de \n final — Files.lines compte quand même la ligne
        Files.writeString(fichier, "seule-ligne", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(1);
    }

    @Test
    @DisplayName("lignes blanches (espaces/tabs) : comptees par compterLignes, ignorees par compterLignesNonVides")
    void lignes_blanches_espaces_et_tabs(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("blanches.txt");
        // 3 lignes blanches (espaces, tabs, mixte) + 1 ligne de contenu = 4 total, 1 non-vide
        Files.writeString(fichier, "   \n\t\t\n  \t  \ncontenu\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(4);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(1);
    }

    @Test
    @DisplayName("accents UTF-8 : lecture correcte, compterLignes renvoie 2")
    void accents_utf8_lus_correctement(@TempDir Path tempDir) throws Exception {
        Path fichier = tempDir.resolve("accents.txt");
        // Contenu accentué écrit et lu explicitement en UTF-8
        Files.writeString(fichier, "réseau\nétablissement\n", StandardCharsets.UTF_8);
        assertThat(CompteurLignes.compterLignes(fichier)).isEqualTo(2);
        assertThat(CompteurLignes.compterLignesNonVides(fichier)).isEqualTo(2);
    }
}
