package piscine.m5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Import d'un fichier CSV de personnel vers une liste de records Personnel.
 *
 * Le fichier CSV est « simple » : une ligne d'en-tête, puis une ligne par
 * membre du personnel, trois champs séparés par des virgules
 * (aucun champ ne contient de virgule — limite assumée). Chaque ligne de
 * données invalide (mauvais nombre de champs, niveau inconnu, ancienneté non
 * numérique) provoque une IllegalArgumentException, la cause d'origine étant
 * chaînée quand elle existe.
 */
public class ImportCsv {

    /**
     * Lit le fichier CSV et construit la liste des membres du personnel.
     *
     * Format attendu :
     * <pre>
     * nom,niveau,anciennete
     * Durand,SENIOR,5
     * Martin,CONFIRME,2
     * </pre>
     *
     * Règles :
     * <ul>
     *   <li>la 1re ligne (en-tête) est sautée ;</li>
     *   <li>les lignes blanches (isBlank) sont ignorées ;</li>
     *   <li>chaque ligne de données est découpée par {@code split(",")} en
     *       exactement 3 champs (sinon IllegalArgumentException) ;</li>
     *   <li>le niveau est converti via {@code Niveau.valueOf} (échec : niveau
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
        // readAllLines charge tout le fichier en mémoire, en UTF-8 explicite
        // (sinon l'encodage par défaut de la JVM pourrait casser les accents).
        List<String> lignes = Files.readAllLines(csv, StandardCharsets.UTF_8);

        List<Personnel> personnels = new ArrayList<>();
        boolean premiere = true;
        for (String ligne : lignes) {
            // La toute première ligne est l'en-tête "nom,niveau,anciennete" : on la saute.
            if (premiere) {
                premiere = false;
                continue;
            }
            // Une ligne blanche (vide ou faite d'espaces) n'est pas une donnée : on l'ignore.
            if (ligne.isBlank()) {
                continue;
            }

            // CSV simple : on découpe sur la virgule. On attend exactement 3 champs.
            String[] champs = ligne.split(",");
            if (champs.length != 3) {
                throw new IllegalArgumentException(
                        "ligne CSV invalide (3 champs attendus) : " + ligne);
            }

            // Conversion du niveau : Niveau.valueOf lève IllegalArgumentException si la
            // constante n'existe pas. On la rattrape pour produire un message métier
            // clair, tout en CHAÎNANT la cause d'origine (2e argument du constructeur).
            Niveau niveau;
            try {
                niveau = Niveau.valueOf(champs[1].trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "niveau inconnu : " + champs[1].trim() + " (ligne : " + ligne + ")", e);
            }

            // Conversion de l'ancienneté : Integer.parseInt lève NumberFormatException
            // sur une valeur non numérique. Même logique : message métier + cause chaînée.
            int anciennete;
            try {
                anciennete = Integer.parseInt(champs[2].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "anciennete invalide : " + champs[2].trim() + " (ligne : " + ligne + ")", e);
            }

            // trim() sur le nom pour tolérer les espaces autour des champs.
            personnels.add(new Personnel(champs[0].trim(), niveau, anciennete));
        }
        return personnels;
    }
}
