package piscine.m5;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Utilitaire d'export d'une liste de personnels membres vers un fichier CSV trié.
 *
 * <p>Le fichier produit contient un en-tête "nom,niveau,anciennete" suivi
 * d'une ligne par personnel, triée par ancienneté décroissante puis nom croissant.</p>
 */
public class ExportCsv {

    // Constructeur privé : classe utilitaire, toutes les méthodes sont statiques.
    private ExportCsv() { }

    /**
     * Exporte la liste de personnels dans le fichier CSV indiqué.
     *
     * <p>Le tri appliqué est : ancienneté décroissante, puis nom alphabétique
     * croissant pour départager les égalités. La liste source n'est jamais mutée.</p>
     *
     * @param personnels liste source (non mutée)
     * @param csv        chemin du fichier de destination (écrasé s'il existe)
     * @throws IOException si l'écriture échoue
     */
    public static void exporter(List<Personnel> personnels, Path csv) throws IOException {
        // TODO
    }
}
