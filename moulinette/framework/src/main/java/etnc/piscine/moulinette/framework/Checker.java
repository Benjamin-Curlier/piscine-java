package etnc.piscine.moulinette.framework;

/**
 * Contrat d'un vérificateur de rendu d'exercice.
 *
 * <p>Un {@code Checker} est responsable d'un aspect précis de l'évaluation :
 * compilation, exécution, style, lint, détection de plagiat, etc.
 * Il reçoit un {@link CheckerContext} décrivant l'exercice et le rendu de l'étudiant,
 * et produit un {@link CheckResult} avec statut, messages pédagogiques
 * et suggestions de correction.
 *
 * <p>Exemple d'implémentation minimale :
 * <pre>{@code
 * public class CompileChecker implements Checker {
 *     public String id() { return "compile"; }
 *
 *     public CheckResult check(CheckerContext ctx) {
 *         // ... lancer javac, analyser la sortie ...
 *         return CheckResult.ok();
 *     }
 * }
 * }</pre>
 */
public interface Checker {

    /**
     * Identifiant court et unique du vérificateur.
     * Exemples : {@code "compile"}, {@code "run"}, {@code "style"}, {@code "plagiat"}.
     *
     * @return identifiant non nul, non vide
     */
    String id();

    /**
     * Exécute la vérification sur le contexte donné.
     *
     * @param context décrit l'exercice et le répertoire rendu par l'étudiant
     * @return résultat de la vérification, jamais {@code null}
     */
    CheckResult check(CheckerContext context);

    /**
     * Indique si un résultat non-OK de ce Checker doit faire échouer l'exercice
     * et stopper la chaîne (bloquant), ou simplement être rapporté (advisory).
     *
     * <p>Par défaut un Checker est bloquant. Le {@code StyleChecker} redéfinit ce
     * contrat à {@code false} pendant la beta (voir backlog #53).
     *
     * @return {@code true} si bloquant (défaut), {@code false} si advisory
     */
    default boolean isBlocking() {
        return true;
    }

    /**
     * Indique si ce vérificateur s'applique à l'exercice décrit par le contexte.
     *
     * <p>Par défaut {@code true} (le checker s'exécute sur tous les exercices) — comportement
     * historique inchangé. Un checker spécifique à un type d'exercice (ex. le
     * {@code MutationChecker}, réservé aux exos « écriture de tests » porteurs d'un dossier
     * {@code mutants/}) redéfinit ce contrat, et les checkers normaux le redéfinissent à
     * {@code false} sur ces exos pour ne pas grader trivialement l'implémentation fournie.
     *
     * @param context décrit l'exercice et le répertoire rendu par l'étudiant
     * @return {@code true} si ce checker doit s'exécuter sur cet exercice (défaut)
     */
    default boolean appliesTo(CheckerContext context) {
        return true;
    }
}
