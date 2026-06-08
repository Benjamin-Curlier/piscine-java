package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Vérificateur des exercices « écriture de tests » (sous-groupe 6.1) : le livrable du stagiaire
 * est <em>la suite de tests</em>, pas le code. Le checker exécute les tests du stagiaire contre
 * l'implémentation <strong>correcte</strong> de référence (ils doivent passer) et contre chaque
 * implémentation <strong>mutée</strong> cachée (ils doivent échouer = « tuer » le mutant).
 *
 * <p>Un test qui passe partout, mutants compris, ne capture rien : il est inutile. Le score
 * <em>proportionnel</em> (mutants tués / total) vit dans le message ; le verdict reste binaire
 * (OK seulement si tous les mutants sont tués), car le modèle {@link CheckResult} ne porte pas
 * de note fractionnaire (cf. spec #57 §3).
 */
public final class MutationChecker implements Checker {

    private final JavaToolkit toolkit;

    public MutationChecker(JavaToolkit toolkit) { this.toolkit = toolkit; }

    @Override public String id() { return "mutation"; }

    /** Ne s'applique qu'aux exos « écriture de tests », reconnus à leur dossier {@code mutants/}. */
    @Override public boolean appliesTo(CheckerContext ctx) { return estEcritureDeTests(ctx); }

    /**
     * Vrai si l'exercice est de type « écriture de tests » : sa référence porte un dossier
     * {@code mutants/}. Utilisé aussi par les checkers normaux pour se désactiver sur ces exos.
     */
    static boolean estEcritureDeTests(CheckerContext ctx) {
        return Files.isDirectory(ctx.exerciseRefPath().resolve("mutants"));
    }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path studentTestSrc = ctx.renduPath().resolve("starter/src/test/java");
        Path correctSrc = ctx.exerciseRefPath().resolve("solution/src/main/java");
        Path mutantsRoot = ctx.exerciseRefPath().resolve("mutants");
        Path build = ctx.renduPath().resolve(".piscine/build/mutation");
        String tooling = toolkit.toolingClasspath();
        try {
            // 1. Compiler l'implémentation correcte de référence.
            Path classesCorrect = build.resolve("classes-correct");
            ProcessResult comCorrect = toolkit.compile(List.of(correctSrc), classesCorrect, tooling);
            if (comCorrect.exitCode() != 0) {
                return CheckResult.error("L'implémentation correcte de référence ne compile pas "
                    + "(problème côté exercice) :\n" + CompileChecker.tronquer(comCorrect.stderr()));
            }

            // 2. Compiler le test du stagiaire contre l'impl correcte (il faut une classe à tester).
            Path classesTest = build.resolve("classes-test");
            String cpCompileTest = tooling + File.pathSeparator + classesCorrect;
            ProcessResult comTest = toolkit.compile(List.of(studentTestSrc), classesTest, cpCompileTest);
            if (comTest.exitCode() != 0) {
                return CheckResult.fail("Ton fichier de test ne compile pas :\n"
                        + CompileChecker.tronquer(comTest.stderr()),
                    "Corrige la syntaxe de ta classe de test avant de la soumettre.");
            }
            List<String> testClasses = FqcnExtractor.fqcnsUnder(studentTestSrc);
            if (testClasses.isEmpty()) {
                return CheckResult.fail("Aucune classe de test trouvée dans ton rendu.",
                    "Écris au moins une classe de test avec des méthodes annotées @Test.");
            }

            // 3. Les tests du stagiaire DOIVENT passer sur l'implémentation correcte.
            String cpCorrect = tooling + File.pathSeparator + classesCorrect + File.pathSeparator + classesTest;
            ProcessResult runCorrect = toolkit.runJUnit(classesTest, cpCorrect, testClasses);
            if (runCorrect.exitCode() != 0) {
                return CheckResult.fail(
                    "Tes tests ne passent pas sur une implémentation correcte — ils sont faux, "
                        + "trop stricts, ou tu n'as écrit aucun test :\n"
                        + AbstractTestChecker.extraitEchecs(runCorrect.stdout()),
                    "Un test juste accepte une implémentation correcte. Vérifie tes valeurs attendues "
                        + "et qu'au moins une méthode @Test existe.");
            }

            // 4. Les tests du stagiaire DOIVENT échouer (tuer) sur chaque mutant.
            List<String> mutantIds = sousDossiers(mutantsRoot);
            List<String> survivants = new ArrayList<>();
            for (String mid : mutantIds) {
                Path classesMut = build.resolve("classes-mut-" + mid);
                ProcessResult comMut = toolkit.compile(List.of(mutantsRoot.resolve(mid)), classesMut, tooling);
                if (comMut.exitCode() != 0) {
                    return CheckResult.error("Le mutant « " + mid + " » ne compile pas "
                        + "(problème côté exercice) :\n" + CompileChecker.tronquer(comMut.stderr()));
                }
                String cpMut = tooling + File.pathSeparator + classesMut + File.pathSeparator + classesTest;
                ProcessResult runMut = toolkit.runJUnit(classesTest, cpMut, testClasses);
                if (runMut.exitCode() == 0) {
                    survivants.add(mid); // aucun test n'a détecté ce mutant : il survit
                }
            }

            int total = mutantIds.size();
            int tues = total - survivants.size();
            if (survivants.isEmpty()) {
                return CheckResult.ok();
            }
            return CheckResult.fail(
                tues + "/" + total + " mutants détectés. Mutants non détectés (tes tests les laissent "
                    + "passer) : " + String.join(", ", survivants) + ".",
                "Ajoute des cas de test qui distinguent une implémentation correcte d'une implémentation "
                    + "buggée — par exemple en couvrant les valeurs limites.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique du MutationChecker : " + e.getMessage());
        }
    }

    /** Liste triée des sous-dossiers directs de {@code root} (un par mutant). */
    private static List<String> sousDossiers(Path root) throws IOException {
        if (!Files.isDirectory(root)) return List.of();
        try (Stream<Path> s = Files.list(root)) {
            return s.filter(Files::isDirectory)
                .map(p -> p.getFileName().toString())
                .sorted()
                .toList();
        }
    }
}
