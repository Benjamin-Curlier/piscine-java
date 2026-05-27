package etnc.piscine.moulinette.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Point d'entrée CLI de la moulinette ETNC.
 *
 * <h2>Usage</h2>
 * <pre>
 *   # Via Maven exec plugin (développement)
 *   mvn -f moulinette/pom.xml -pl cli exec:java \
 *       -Dexec.mainClass="etnc.piscine.moulinette.cli.Main" \
 *       -Dexec.args="run --exo 1.1.1 --rendu /chemin/vers/rendu"
 * </pre>
 *
 * <h2>Commandes disponibles</h2>
 * <ul>
 *   <li>{@code run --exo <id> --rendu <chemin>} — évalue un rendu</li>
 *   <li>{@code --help} / {@code -h} — affiche l'aide</li>
 * </ul>
 *
 * <p><em>Note :</em> le parsing d'arguments sera migré vers <strong>picocli</strong>
 * quand le périmètre CLI sera stabilisé.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0 || isHelpFlag(args[0])) {
            printHelp();
            return;
        }

        switch (args[0]) {
            case "run"  -> runCommand(args);
            default     -> {
                System.err.println("Commande inconnue : " + args[0]);
                printHelp();
                System.exit(1);
            }
        }
    }

    // ── Commandes ─────────────────────────────────────────────────────────────

    private static void runCommand(String[] args) {
        String exoId    = null;
        String renduPath = null;

        for (int i = 1; i < args.length - 1; i++) {
            if ("--exo".equals(args[i]))   exoId    = args[i + 1];
            if ("--rendu".equals(args[i])) renduPath = args[i + 1];
        }

        if (exoId == null || renduPath == null) {
            System.err.println("Usage : run --exo <id> --rendu <chemin>");
            System.err.println("Exemple : run --exo 1.1.1 --rendu ./monrendu/");
            System.exit(1);
        }

        log.info("Évaluation de l'exercice {} — rendu : {}", exoId, renduPath);
        System.out.printf("Évaluation de l'exercice %s en cours...%n", exoId);
        System.out.println("TODO : pipeline de vérification à implémenter.");
        System.out.println("       (framework → runner → reports)");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static boolean isHelpFlag(String arg) {
        return "--help".equals(arg) || "-h".equals(arg);
    }

    private static void printHelp() {
        System.out.println("""
                Moulinette ETNC v0.1.0-SNAPSHOT

                Usage :
                  run --exo <id> --rendu <chemin>    Évalue le rendu d'un exercice
                  --help | -h                        Affiche cette aide

                Exemple :
                  run --exo 1.1.1 --rendu ./monrendu/
                """);
    }
}
