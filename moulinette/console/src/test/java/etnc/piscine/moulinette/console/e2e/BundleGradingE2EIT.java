package etnc.piscine.moulinette.console.e2e;

import etnc.piscine.moulinette.console.checkers.JavaToolkit;
import etnc.piscine.moulinette.console.checkers.MutationChecker;
import etnc.piscine.moulinette.console.checkers.PrivateTestChecker;
import etnc.piscine.moulinette.console.checkers.PublicTestChecker;
import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validation RC du chemin de notation « console » — celui du <strong>bundle standalone</strong>
 * (javac + JUnit ConsoleLauncher, <em>sans Maven</em>) — sur de VRAIS exercices des modules 4 à 6,
 * jamais exercés en standalone avant cette RC. On simule un rendu parfait (la solution de
 * référence injectée dans le starter) et on vérifie que la moulinette le note OK.
 *
 * <p>Couvre en particulier les deux chemins de notation nouveaux du module 6 :
 * <ul>
 *   <li><b>6.1</b> : le {@link MutationChecker} (le livrable est le test du stagiaire) ;</li>
 *   <li><b>6.2</b> : des tests d'exercice qui <b>forkent {@code git}</b> depuis le JVM forké par
 *       la moulinette (double fork java → git) — git doit être résoluble (MinGit dans le bundle).</li>
 * </ul>
 */
@Tag("e2e")
class BundleGradingE2EIT {

    private static final Path REPO = repoRoot();
    private final JavaToolkit tk = new JavaToolkit(new ProcessRunner());

    @Test
    void module4_collection_solution_passe_publics_et_prives(@TempDir Path tmp) throws IOException {
        CheckerContext ctx = renduAvecCodeSolution(tmp, "module-4-collections-generiques-lambdas/4.1.1-annuaire-militaire");
        assertThat(new PublicTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
        assertThat(new PrivateTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void module5_io_solution_passe_publics_et_prives(@TempDir Path tmp) throws IOException {
        CheckerContext ctx = renduAvecCodeSolution(tmp, "module-5-exceptions-io/5.2.1-compteur-lignes");
        assertThat(new PublicTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
        assertThat(new PrivateTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void module6_ecriture_de_tests_suite_modele_donne_mutation_OK(@TempDir Path tmp) throws IOException {
        CheckerContext ctx = renduAvecTestsSolution(tmp, "module-6-tests-git/6.1.1-tests-classe-existante");
        assertThat(new MutationChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void module6_git_commit_propre_solution_passe(@TempDir Path tmp) throws IOException {
        CheckerContext ctx = renduAvecCodeSolution(tmp, "module-6-tests-git/6.2.1-commit-propre");
        assertThat(new PublicTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
        assertThat(new PrivateTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void module6_git_resolution_conflit_solution_passe(@TempDir Path tmp) throws IOException {
        CheckerContext ctx = renduAvecCodeSolution(tmp, "module-6-tests-git/6.2.2-resolution-conflit");
        assertThat(new PublicTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
        assertThat(new PrivateTestChecker(tk).check(ctx).status()).isEqualTo(CheckResult.Status.OK);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Rendu « écriture de code » : starter + le code main de la solution de référence. */
    private static CheckerContext renduAvecCodeSolution(Path tmp, String exoRel) throws IOException {
        Path exoRef = REPO.resolve("exercises").resolve(exoRel);
        Path rendu = tmp.resolve("ws");
        copyTree(exoRef.resolve("starter"), rendu.resolve("starter"));
        copyTree(exoRef.resolve("solution/src/main/java"), rendu.resolve("starter/src/main/java"));
        return new CheckerContext(idDe(exoRel), rendu, exoRef);
    }

    /** Rendu « écriture de tests » : starter + la suite de tests modèle de la solution. */
    private static CheckerContext renduAvecTestsSolution(Path tmp, String exoRel) throws IOException {
        Path exoRef = REPO.resolve("exercises").resolve(exoRel);
        Path rendu = tmp.resolve("ws");
        copyTree(exoRef.resolve("starter"), rendu.resolve("starter"));
        copyTree(exoRef.resolve("solution/src/test/java"), rendu.resolve("starter/src/test/java"));
        return new CheckerContext(idDe(exoRel), rendu, exoRef);
    }

    private static String idDe(String exoRel) {
        return Path.of(exoRel).getFileName().toString().split("-")[0]; // "6.2.2-..." -> "6.2.2"
    }

    private static void copyTree(Path src, Path dst) throws IOException {
        if (!Files.isDirectory(src)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(src)) {
            for (Path p : walk.toList()) {
                Path target = dst.resolve(src.relativize(p));
                if (Files.isDirectory(p)) {
                    Files.createDirectories(target);
                } else {
                    Files.createDirectories(target.getParent());
                    Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static Path repoRoot() {
        Path p = Path.of("").toAbsolutePath();
        while (p != null && !Files.isDirectory(p.resolve("exercises"))) {
            p = p.getParent();
        }
        if (p == null) {
            throw new IllegalStateException("racine du repo (dossier avec exercises/) introuvable");
        }
        return p;
    }
}
