package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Le {@link MutationChecker} grade les tests ÉCRITS PAR LE STAGIAIRE : ils doivent passer sur
 * l'implémentation correcte de référence et échouer (tuer) sur chaque mutant caché.
 */
@Tag("tools")
class MutationCheckerIT {

    private final JavaToolkit tk = new JavaToolkit(new ProcessRunner());

    private static final String CORRECT =
        "package etnc.m6;\npublic class Addition {\n  public int somme(int a, int b) { return a + b; }\n}\n";
    private static final String MUTANT_INVERSE =
        "package etnc.m6;\npublic class Addition {\n  public int somme(int a, int b) { return a - b; }\n}\n";
    private static final String MUTANT_CONSTANTE =
        "package etnc.m6;\npublic class Addition {\n  public int somme(int a, int b) { return 0; }\n}\n";

    /** Test stagiaire RIGOUREUX : couvre deux cas qui distinguent le correct des deux mutants. */
    private static final String TEST_BON = """
        package etnc.m6;
        import org.junit.jupiter.api.Test;
        import static org.assertj.core.api.Assertions.assertThat;
        class AdditionTest {
          @Test void sommePositifs() { assertThat(new Addition().somme(2, 3)).isEqualTo(5); }
          @Test void sommeAvecNegatif() { assertThat(new Addition().somme(-1, 1)).isEqualTo(0); }
        }
        """;

    @Test
    void tests_rigoureux_passent_sur_le_correct_et_tuent_tous_les_mutants(@TempDir Path tmp) throws IOException {
        Map<String, String> mutants = new LinkedHashMap<>();
        mutants.put("operateur-inverse", MUTANT_INVERSE);
        mutants.put("retour-constant", MUTANT_CONSTANTE);
        CheckerContext ctx = scenario(tmp, TEST_BON, CORRECT, mutants);

        CheckResult r = new MutationChecker(tk).check(ctx);

        assertThat(r.status()).isEqualTo(CheckResult.Status.OK);
    }

    @Test
    void tests_faux_sur_le_correct_echouent(@TempDir Path tmp) throws IOException {
        // Test stagiaire FAUX : attend 6 pour somme(2,3). Échoue déjà sur l'impl correcte.
        String testFaux = """
            package etnc.m6;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class AdditionTest {
              @Test void faux() { assertThat(new Addition().somme(2, 3)).isEqualTo(6); }
            }
            """;
        Map<String, String> mutants = new LinkedHashMap<>();
        mutants.put("operateur-inverse", MUTANT_INVERSE);
        CheckerContext ctx = scenario(tmp, testFaux, CORRECT, mutants);

        CheckResult r = new MutationChecker(tk).check(ctx);

        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages().toString()).contains("implémentation correcte");
    }

    @Test
    void un_mutant_survivant_fait_echouer_et_est_nomme(@TempDir Path tmp) throws IOException {
        // Test FAIBLE : somme(0,0)==0 passe sur le correct ET sur le mutant a-b (0-0==0).
        String testFaible = """
            package etnc.m6;
            import org.junit.jupiter.api.Test;
            import static org.assertj.core.api.Assertions.assertThat;
            class AdditionTest {
              @Test void faible() { assertThat(new Addition().somme(0, 0)).isEqualTo(0); }
            }
            """;
        Map<String, String> mutants = new LinkedHashMap<>();
        mutants.put("operateur-inverse", MUTANT_INVERSE);
        CheckerContext ctx = scenario(tmp, testFaible, CORRECT, mutants);

        CheckResult r = new MutationChecker(tk).check(ctx);

        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages().toString())
            .contains("operateur-inverse")
            .contains("0/1");
    }

    @Test
    void aucun_test_ecrit_echoue(@TempDir Path tmp) throws IOException {
        // Classe de test SANS méthode @Test : rien ne détecte quoi que ce soit.
        String testVide = """
            package etnc.m6;
            class AdditionTest {
              void pasUnTest() { }
            }
            """;
        Map<String, String> mutants = new LinkedHashMap<>();
        mutants.put("operateur-inverse", MUTANT_INVERSE);
        CheckerContext ctx = scenario(tmp, testVide, CORRECT, mutants);

        CheckResult r = new MutationChecker(tk).check(ctx);

        assertThat(r.status()).isEqualTo(CheckResult.Status.FAIL);
        assertThat(r.messages()).isNotEmpty();
    }

    @Test
    void mutant_qui_ne_compile_pas_est_une_erreur_exo(@TempDir Path tmp) throws IOException {
        Map<String, String> mutants = new LinkedHashMap<>();
        mutants.put("casse", "package etnc.m6;\npublic class Addition {\n  public int somme(int a, int b) { return a + ; }\n}\n");
        CheckerContext ctx = scenario(tmp, TEST_BON, CORRECT, mutants);

        CheckResult r = new MutationChecker(tk).check(ctx);

        assertThat(r.status()).isEqualTo(CheckResult.Status.ERROR);
        assertThat(r.messages().toString()).contains("casse");
    }

    // ── Fixture ──────────────────────────────────────────────────────────────

    /**
     * Monte un exo « écriture de tests » : le test du stagiaire sous {@code starter/src/test/java},
     * l'impl correcte sous {@code solution/src/main/java}, et un dossier {@code mutants/<id>/} par mutant.
     */
    private static CheckerContext scenario(Path tmp, String testSource, String correctSource,
                                           Map<String, String> mutants) throws IOException {
        Path rendu = tmp.resolve("ws/exo");
        Path testDir = rendu.resolve("starter/src/test/java/etnc/m6");
        Files.createDirectories(testDir);
        Files.writeString(testDir.resolve("AdditionTest.java"), testSource);

        Path ref = tmp.resolve("piscine/exo");
        Path correctDir = ref.resolve("solution/src/main/java/etnc/m6");
        Files.createDirectories(correctDir);
        Files.writeString(correctDir.resolve("Addition.java"), correctSource);

        for (var e : mutants.entrySet()) {
            Path mDir = ref.resolve("mutants").resolve(e.getKey()).resolve("etnc/m6");
            Files.createDirectories(mDir);
            Files.writeString(mDir.resolve("Addition.java"), e.getValue());
        }

        return new CheckerContext("6.1.1", rendu, ref);
    }
}
