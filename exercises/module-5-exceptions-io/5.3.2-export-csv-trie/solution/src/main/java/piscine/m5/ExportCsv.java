package piscine.m5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
        // Copie défensive : on ne trie jamais la liste passée en paramètre
        // (respecter le contrat "source non mutée" attendu par l'appelant).
        List<Personnel> copie = new ArrayList<>(personnels);

        // Tri idiomatique M4 : ancienneté décroissante (.reversed()),
        // puis nom alphabétique croissant pour égalité d'ancienneté.
        copie.sort(Comparator.comparingInt(Personnel::anciennete)
                             .reversed()
                             .thenComparing(Personnel::nom));

        // Construction des lignes CSV : en-tête obligatoire en première position,
        // puis une ligne par personnel avec String.join (pas de concaténation manuelle).
        List<String> lignes = new ArrayList<>();
        lignes.add("nom,niveau,anciennete");
        for (Personnel p : copie) {
            // niveau().name() renvoie le nom de la constante enum ("SENIOR", etc.)
            lignes.add(String.join(",", p.nom(), p.niveau().name(), String.valueOf(p.anciennete())));
        }

        // Files.write écrase le fichier s'il existe déjà (comportement par défaut).
        // UTF-8 explicite pour la portabilité (Windows peut avoir un charset par défaut différent).
        Files.write(csv, lignes, StandardCharsets.UTF_8);
    }
}
