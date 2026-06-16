package piscine.m5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lecteur de fichier de configuration au format {@code cle=valeur} (une paire
 * par ligne), parsé à la main — SANS {@link java.util.Properties} (réservé à un
 * chapitre ultérieur).
 *
 * Conventions du format attendu :
 * - une ligne {@code cle=valeur} par paire ;
 * - les lignes blanches sont ignorées ;
 * - les lignes commençant par {@code #} (après trim) sont des commentaires, ignorées ;
 * - la coupure se fait au PREMIER {@code =} (une valeur peut donc contenir des {@code =}) ;
 * - la clé et la valeur sont rognées (trim) ;
 * - en cas de clé dupliquée, la DERNIÈRE occurrence l'emporte.
 *
 * Les chemins sont FOURNIS par l'appelant ; la lecture propage {@link IOException}.
 */
public class LectureConfig {

    /**
     * Lit un fichier de configuration et renvoie la table {@code cle -> valeur}.
     *
     * @param config chemin du fichier de configuration (fourni par l'appelant)
     * @return la table des paires clé/valeur lues (clé dupliquée : la dernière gagne)
     * @throws IOException si la lecture du fichier échoue
     */
    public static Map<String, String> lireConfig(Path config) throws IOException {
        // UTF-8 explicite : un fichier de config peut contenir des accents (libellés métier).
        List<String> lignes = Files.readAllLines(config, StandardCharsets.UTF_8);

        // LinkedHashMap : on conserve l'ordre d'apparition des clés (lisibilité au débogage).
        // Le put écrase une clé déjà présente, ce qui réalise « la dernière occurrence gagne ».
        Map<String, String> configuration = new LinkedHashMap<>();

        for (String ligne : lignes) {
            // On ignore les lignes vides ou composées uniquement d'espaces/tabs.
            if (ligne.isBlank()) {
                continue;
            }
            // On ignore les commentaires : une ligne dont le premier caractère non blanc est '#'.
            if (ligne.trim().startsWith("#")) {
                continue;
            }
            // Une ligne sans '=' n'est pas une paire valide : on l'ignore silencieusement.
            int positionEgal = ligne.indexOf('=');
            if (positionEgal < 0) {
                continue;
            }
            // Coupure au PREMIER '=' : la clé est avant, la valeur est TOUT le reste
            // (qui peut donc contenir d'autres '=', ex. une URL avec ?a=b).
            String cle = ligne.substring(0, positionEgal).trim();
            String valeur = ligne.substring(positionEgal + 1).trim();
            configuration.put(cle, valeur);
        }

        return configuration;
    }

    /**
     * Renvoie la valeur associée à une clé, ou une valeur par défaut si la clé
     * est absente de la configuration.
     *
     * @param config           chemin du fichier de configuration (fourni par l'appelant)
     * @param cle              clé recherchée
     * @param valeurParDefaut  valeur renvoyée si la clé est absente
     * @return la valeur de la clé, ou {@code valeurParDefaut} si elle n'existe pas
     * @throws IOException si la lecture du fichier échoue
     */
    public static String lireValeur(Path config, String cle, String valeurParDefaut) throws IOException {
        // On délègue à lireConfig pour ne pas dupliquer la logique de parsing ;
        // getOrDefault encapsule proprement le cas « clé absente ».
        return lireConfig(config).getOrDefault(cle, valeurParDefaut);
    }
}
