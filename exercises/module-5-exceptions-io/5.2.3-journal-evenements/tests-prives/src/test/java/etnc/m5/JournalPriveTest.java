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
 * Tests privés de l'exercice 5.2.3 (cas limites du journal append-only).
 */
@DisplayName("Journal — tests prives")
class JournalPriveTest {

    @Test
    @DisplayName("dix ajouter successifs produisent dix lignes dans l'ordre")
    void dix_ajouter_dix_lignes(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        for (int i = 1; i <= 10; i++) {
            Journal.ajouter(journal, "evt" + i);
        }

        List<String> lignes = Journal.lire(journal);
        assertThat(lignes).hasSize(10);
        assertThat(lignes).containsExactly(
                "evt1", "evt2", "evt3", "evt4", "evt5",
                "evt6", "evt7", "evt8", "evt9", "evt10");
    }

    @Test
    @DisplayName("lire ne supprime pas les lignes existantes (idempotent)")
    void lire_idempotent(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "alpha");
        Journal.ajouter(journal, "beta");

        // Deux appels successifs à lire renvoient le même contenu
        List<String> premiere = Journal.lire(journal);
        List<String> deuxieme = Journal.lire(journal);
        assertThat(premiere).isEqualTo(deuxieme);
    }

    @Test
    @DisplayName("alternance ajouter/lire coherente : lire reflete l'etat courant")
    void alternance_ajouter_lire(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "alpha");
        assertThat(Journal.lire(journal)).containsExactly("alpha");

        Journal.ajouter(journal, "beta");
        assertThat(Journal.lire(journal)).containsExactly("alpha", "beta");

        Journal.ajouter(journal, "gamma");
        assertThat(Journal.lire(journal)).containsExactly("alpha", "beta", "gamma");
    }

    @Test
    @DisplayName("lire ne trim pas les lignes (espaces conserves tels quels)")
    void lire_ne_trim_pas(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "  avec espaces  ");

        assertThat(Journal.lire(journal)).containsExactly("  avec espaces  ");
    }

    @Test
    @DisplayName("contenu brut du fichier se termine par \\n apres chaque ajouter")
    void contenu_fichier_termine_par_newline(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "ligne");

        // Le contenu brut doit être "ligne\n" — chaque événement occupe une ligne complète
        String contenu = Files.readString(journal, StandardCharsets.UTF_8);
        assertThat(contenu).isEqualTo("ligne\n");
    }

    @Test
    @DisplayName("ajouter plusieurs fois sur fichier pre-rempli conserve tout l'historique")
    void historique_complet_preserv(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");
        // Simule un journal déjà alimenté avec deux entrées
        Files.writeString(journal, "entree1\nentree2\n", StandardCharsets.UTF_8);

        Journal.ajouter(journal, "entree3");
        Journal.ajouter(journal, "entree4");

        assertThat(Journal.lire(journal))
                .containsExactly("entree1", "entree2", "entree3", "entree4");
    }

    @Test
    @DisplayName("deux journaux independants ne s'interferent pas")
    void deux_journaux_independants(@TempDir Path tempDir) throws Exception {
        Path journal1 = tempDir.resolve("journal1.txt");
        Path journal2 = tempDir.resolve("journal2.txt");

        Journal.ajouter(journal1, "ops1");
        Journal.ajouter(journal2, "ops2");
        Journal.ajouter(journal1, "ops3");

        assertThat(Journal.lire(journal1)).containsExactly("ops1", "ops3");
        assertThat(Journal.lire(journal2)).containsExactly("ops2");
    }

    @Test
    @DisplayName("evenement vide seul : le fichier contient exactement une ligne vide")
    void evenement_vide_seul(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "");

        List<String> lignes = Journal.lire(journal);
        assertThat(lignes).hasSize(1);
        assertThat(lignes.get(0)).isEmpty();
    }
}
