package piscine.m5;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Utilitaire de copie de fichier octet-exact via NIO.2.
 *
 * La copie utilise Files.copy avec l'option REPLACE_EXISTING pour transférer
 * les octets sans interprétation textuelle, en écrasant la destination si elle
 * existe déjà.
 */
public class CopieFichier {

    /**
     * Copie le contenu de source vers destination, octet pour octet.
     * Si destination existe déjà, elle est écrasée.
     *
     * @param source      le fichier à copier
     * @param destination l'emplacement de la copie
     * @throws IOException en cas d'erreur d'accès aux fichiers
     */
    public static void copier(Path source, Path destination) throws IOException {
        // TODO
    }
}
