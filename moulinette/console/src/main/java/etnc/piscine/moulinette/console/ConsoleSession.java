package etnc.piscine.moulinette.console;

import etnc.piscine.moulinette.console.checkers.CompileChecker;
import etnc.piscine.moulinette.console.checkers.JavaToolkit;
import etnc.piscine.moulinette.console.checkers.MutationChecker;
import etnc.piscine.moulinette.console.checkers.PrivateTestChecker;
import etnc.piscine.moulinette.console.checkers.PublicTestChecker;
import etnc.piscine.moulinette.console.checkers.StyleChecker;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.commands.CommandResult;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.ReplContext;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.ExerciseCatalog;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.runner.ProcessRunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Façade « bibliothèque » de la console : une session = un workspace stagiaire câblé
 * (git, catalogue d'exos, checkers, trigger d'évaluation). Tout front (REPL terminal,
 * GUI web) exécute des lignes de commande via {@link #execute(String)} et reçoit un
 * {@link CommandResult} — aucune écriture directe sur stdout.
 */
public final class ConsoleSession {

    private final ReplContext ctx;
    private final CommandRegistry registry;

    private ConsoleSession(ReplContext ctx, CommandRegistry registry) {
        this.ctx = ctx;
        this.registry = registry;
    }

    /** Câblage complet sur un workspace réel (extrait de Main.runRepl). */
    public static ConsoleSession open(Path repo, Path piscineRepo) {
        var git = new ProcessGitClient();
        ExerciseCatalog catalog = ExerciseCatalog.scan(piscineRepo.resolve("exercises"));
        var toolkit = new JavaToolkit(new ProcessRunner());
        Path styleConfig = StyleChecker.extractBundledConfig();
        List<Checker> checkers = List.of(
            new CompileChecker(toolkit),
            new PublicTestChecker(toolkit),
            new PrivateTestChecker(toolkit),
            new MutationChecker(toolkit), // ne s'active que sur les exos « écriture de tests » (mutants/)
            new StyleChecker(toolkit, styleConfig));
        var runner = new MoulinetteRunner.Default(catalog, checkers, repo.resolve(".piscine/reports"));
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(repo, git, catalog, Mode.LOCAL);
        return new ConsoleSession(ctx, CommandRegistry.defaults(trigger));
    }

    /** Assemblage manuel (tests, fronts custom) : contexte et registry fournis par l'appelant. */
    public static ConsoleSession of(ReplContext ctx, CommandRegistry registry) {
        return new ConsoleSession(ctx, registry);
    }

    /** Tokenise (guillemets doubles respectés) puis dispatch une ligne de commande. */
    public CommandResult execute(String line) {
        return registry.dispatch(ctx, tokenize(line));
    }

    public String currentBranch() { return ctx.currentBranch(); }

    public Path repoRoot() { return ctx.repoRoot(); }

    /** Catalogue des exercices (peut être null sur un assemblage manuel de test). */
    public ExerciseCatalog catalog() { return ctx.catalog(); }

    /**
     * Découpe une ligne en jetons, en respectant les guillemets doubles.
     * Exemple : {@code git commit -m "mon message"} → [git, commit, -m, mon message].
     */
    static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        boolean hasToken = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                hasToken = true;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (hasToken) { tokens.add(cur.toString()); cur.setLength(0); hasToken = false; }
            } else {
                cur.append(c);
                hasToken = true;
            }
        }
        if (hasToken) tokens.add(cur.toString());
        return tokens;
    }
}
