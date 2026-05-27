package etnc.piscine.moulinette.runner;

/**
 * Résultat d'une exécution lancée par {@link ProcessRunner}.
 *
 * @param exitCode code de retour du processus (0 = succès POSIX)
 * @param stdout   sortie standard capturée (peut être vide, jamais null)
 * @param stderr   sortie d'erreur capturée (peut être vide, jamais null)
 */
public record ProcessResult(int exitCode, String stdout, String stderr) {

    public ProcessResult {
        stdout = stdout == null ? "" : stdout;
        stderr = stderr == null ? "" : stderr;
    }

    /**
     * @return {@code true} si le code de retour vaut 0
     */
    public boolean success() {
        return exitCode == 0;
    }
}
