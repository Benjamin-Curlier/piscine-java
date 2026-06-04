package etnc.m5;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Import d'un fichier CSV de personnel vers une liste de records Personnel.
 *
 * Le fichier CSV est « simple » : une ligne d'en-tête, puis une ligne par
 * membre du personnel, trois champs séparés par des virgules
 * (aucun champ ne contient de virgule — limite assumée). Chaque ligne de
 * données invalide (mauvais nombre de champs, grade inconnu, ancienneté non
 * numérique) provoque une IllegalArgumentException, la cause d'origine étant
 * chaînée quand elle existe.
 */
public class ImportCsv {

    /**
     * Lit le fichier CSV et construit la liste des membres du personnel.
     *
     * Format attendu :
     * <pre>
     * nom,grade,anciennete
     * Durand,SERGENT,5
     * Martin,CAPORAL,2
     * </pre>
     *
     * Règles :
     * <ul>
     *   <li>la 1re ligne (en-tête) est sautée ;</li>
     *   <li>les lignes blanches (isBlank) sont ignorées ;</li>
     *   <li>chaque ligne de données est découpée par {@code split(",")} en
     *       exactement 3 champs (sinon IllegalArgumentException) ;</li>
     *   <li>le grade est converti via {@code Grade.valueOf} (échec : grade
     *       inconnu → IllegalArgumentException, cause chaînée) ;</li>
     *   <li>l'ancienneté est convertie via {@code Integer.parseInt} (échec :
     *       ancienneté non numérique → IllegalArgumentException, cause chaînée) ;</li>
     *   <li>chaque champ est nettoyé avec {@code trim()}.</li>
     * </ul>
     *
     * @param csv chemin vers le fichier CSV à importer (fourni par l'appelant)
     * @return la liste des membres du personnel, dans l'ordre du fichier
     * @throws IOException              si le fichier ne peut pas être lu
     * @throws IllegalArgumentException si une ligne de données est invalide
     */
    public static List<Personnel> importer(Path csv) throws IOException {
        return List.of(); // TODO
    }
}
