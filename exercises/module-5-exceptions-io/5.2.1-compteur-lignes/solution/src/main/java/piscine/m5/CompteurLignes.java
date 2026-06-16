package piscine.m5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Utilitaire de comptage de lignes dans un fichier texte.
 *
 * Expose deux méthodes statiques qui lisent un fichier via NIO.2 (Files.lines)
 * et renvoient un décompte de lignes. Le fichier est fourni par l'appelant
 * sous forme de Path — aucun chemin n'est codé en dur.
 */
public class CompteurLignes {

    /**
     * Compte le nombre total de lignes dans le fichier.
     *
     * Les lignes vides et les lignes blanches (espaces, tabulations) sont
     * incluses dans le décompte.
     *
     * @param fichier chemin vers le fichier à analyser (fourni par l'appelant)
     * @return nombre total de lignes
     * @throws IOException si le fichier ne peut pas être lu
     */
    public static long compterLignes(Path fichier) throws IOException {
        // Files.lines maintient un descripteur de fichier ouvert — try-with-resources
        // garantit la fermeture du flux même si count() lève une exception.
        try (Stream<String> flux = Files.lines(fichier, StandardCharsets.UTF_8)) {
            return flux.count();
        }
    }

    /**
     * Compte le nombre de lignes non blanches dans le fichier.
     *
     * Une ligne est considérée blanches si String.isBlank() renvoie true
     * (ligne vide ou composée uniquement d'espaces/tabulations).
     *
     * @param fichier chemin vers le fichier à analyser (fourni par l'appelant)
     * @return nombre de lignes dont le contenu n'est pas blanc
     * @throws IOException si le fichier ne peut pas être lu
     */
    public static long compterLignesNonVides(Path fichier) throws IOException {
        // isBlank() couvre les cas "", " ", "\t\t " — plus complet que isEmpty().
        try (Stream<String> flux = Files.lines(fichier, StandardCharsets.UTF_8)) {
            return flux.filter(ligne -> !ligne.isBlank()).count();
        }
    }
}
