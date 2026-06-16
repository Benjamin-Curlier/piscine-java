package piscine.m5;

import java.io.IOException;
import java.nio.file.Path;
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
        return Map.of(); // TODO
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
        return null; // TODO
    }
}
