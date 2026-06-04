package etnc.m5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Gestion d'un journal d'événements append-only.
 * Chaque événement est ajouté en fin de fichier sans écraser l'existant.
 */
public class Journal {

    private Journal() {
        // Classe utilitaire — pas d'instanciation
    }

    /**
     * Ajoute un événement en fin du journal.
     * Le fichier est créé s'il n'existe pas encore.
     *
     * @param journal   chemin vers le fichier journal
     * @param evenement texte de l'événement à enregistrer
     * @throws IOException en cas d'erreur d'accès au fichier
     */
    public static void ajouter(Path journal, String evenement) throws IOException {
        // CREATE ouvre le fichier existant ou le crée s'il est absent.
        // APPEND déplace le curseur en fin de fichier avant chaque écriture,
        // garantissant que le contenu précédent n'est jamais écrasé.
        Files.writeString(journal, evenement + "\n", StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Lit toutes les lignes du journal.
     * Renvoie une liste vide si le journal n'a pas encore été créé.
     *
     * @param journal chemin vers le fichier journal
     * @return liste des événements enregistrés, dans l'ordre d'insertion
     * @throws IOException en cas d'erreur d'accès au fichier
     */
    public static List<String> lire(Path journal) throws IOException {
        // Protège contre la NoSuchFileException que readAllLines lèverait
        // si le journal n'a encore reçu aucun événement.
        if (!Files.exists(journal)) {
            return List.of();
        }
        return Files.readAllLines(journal, StandardCharsets.UTF_8);
    }
}
