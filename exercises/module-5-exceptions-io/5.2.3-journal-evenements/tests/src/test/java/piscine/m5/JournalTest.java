package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 5.2.3 (journal d'événements append-only).
 */
@DisplayName("Journal — tests publics")
class JournalTest {

    @Test
    @DisplayName("ajouter un evenement puis lire renvoie une liste a un element")
    void ajouter_un_lire_un(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "démarrage");

        assertThat(Journal.lire(journal)).containsExactly("démarrage");
    }

    @Test
    @DisplayName("deux ajouter successifs renvoient les deux lignes dans l'ordre")
    void deux_ajouter_ordre_preserve(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "premier");
        Journal.ajouter(journal, "deuxieme");

        assertThat(Journal.lire(journal)).containsExactly("premier", "deuxieme");
    }

    @Test
    @DisplayName("lire sur un journal absent renvoie une liste vide")
    void lire_journal_absent_liste_vide(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("inexistant.txt");

        assertThat(Journal.lire(journal)).isEmpty();
    }

    @Test
    @DisplayName("le fichier est cree au premier ajouter")
    void fichier_cree_au_premier_ajouter(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");
        assertThat(Files.exists(journal)).isFalse();

        Journal.ajouter(journal, "creation");

        assertThat(Files.exists(journal)).isTrue();
    }

    @Test
    @DisplayName("chaque ajouter ajoute exactement une ligne")
    void chaque_ajouter_une_ligne(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "ligne1");
        assertThat(Journal.lire(journal)).hasSize(1);

        Journal.ajouter(journal, "ligne2");
        assertThat(Journal.lire(journal)).hasSize(2);

        Journal.ajouter(journal, "ligne3");
        assertThat(Journal.lire(journal)).hasSize(3);
    }

    @Test
    @DisplayName("les accents et caracteres speciaux sont preserves (UTF-8)")
    void accents_preserves(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "Opération Éclipse — données sensibles");

        assertThat(Journal.lire(journal))
                .containsExactly("Opération Éclipse — données sensibles");
    }

    @Test
    @DisplayName("ajouter sur un fichier pre-rempli ajoute apres le contenu existant")
    void ajouter_sur_fichier_existant(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");
        // Pré-remplissage manuel simule un journal déjà en cours
        Files.writeString(journal, "ancien\n", StandardCharsets.UTF_8);

        Journal.ajouter(journal, "nouveau");

        assertThat(Journal.lire(journal)).containsExactly("ancien", "nouveau");
    }

    @Test
    @DisplayName("un evenement vide ajoute une ligne vide (taille totale + 1)")
    void evenement_vide_ajoute_ligne_vide(@TempDir Path tempDir) throws Exception {
        Path journal = tempDir.resolve("journal.txt");

        Journal.ajouter(journal, "avant");
        Journal.ajouter(journal, "");
        Journal.ajouter(journal, "apres");

        assertThat(Journal.lire(journal)).containsExactly("avant", "", "apres");
    }
}
