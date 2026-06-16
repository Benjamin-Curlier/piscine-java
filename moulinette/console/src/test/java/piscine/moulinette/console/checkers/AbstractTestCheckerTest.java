package piscine.moulinette.console.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Vérifie que {@code extraitEchecs} produit un résumé lisible (attendu/obtenu), pas un dump brut. */
@DisplayName("AbstractTestChecker — résumé d'échecs")
class AbstractTestCheckerTest {

    // Sortie typique du ConsoleLauncher (--details=summary) : une assertion AssertJ + une exception.
    private static final String SORTIE = """
            Failures (2):
              JUnit Jupiter:Fiche — tests publics:affiche la fiche au format attendu
                MethodSource [className = 'piscine.m1.AffichageFormateTest', methodName = 'affiche_la_fiche', methodParameterTypes = '']
                => org.opentest4j.AssertionFailedError: [La fiche doit etre exacte]\s
            expected: "=== Fiche membre ===\\nNom    : Martin"
             but was: ""
                   piscine.m1.AffichageFormateTest.affiche_la_fiche(AffichageFormateTest.java:24)
                   java.base/java.lang.reflect.Method.invoke(Method.java:565)
              JUnit Jupiter:Calcul — tests publics:ne leve pas d'exception
                MethodSource [className = 'piscine.m1.CalculTest', methodName = 'pas_exception', methodParameterTypes = '']
                => java.lang.NullPointerException: Cannot invoke "String.length()" because "s" is null
                   piscine.m1.CalculTest.pas_exception(CalculTest.java:12)

            Test run finished after 100 ms
            [         2 tests failed           ]
            """;

    @Test
    @DisplayName("présente l'intitulé, l'attendu et l'obtenu d'une assertion")
    void assertion_lisible() {
        String resume = AbstractTestChecker.extraitEchecs(SORTIE);

        assertThat(resume)
            .contains("✗ affiche la fiche au format attendu")
            .contains("La fiche doit etre exacte")
            .contains("attendu : \"=== Fiche membre ===")
            .contains("obtenu  : \"\"");
    }

    @Test
    @DisplayName("présente une exception non-assertion proprement")
    void exception_lisible() {
        String resume = AbstractTestChecker.extraitEchecs(SORTIE);

        assertThat(resume)
            .contains("✗ ne leve pas d'exception")
            .contains("a échoué : NullPointerException");
    }

    @Test
    @DisplayName("élimine le bruit (MethodSource, stack frames, décompte)")
    void sans_bruit() {
        String resume = AbstractTestChecker.extraitEchecs(SORTIE);

        assertThat(resume)
            .doesNotContain("MethodSource")
            .doesNotContain("java.base/")
            .doesNotContain("Method.invoke")
            .doesNotContain("tests failed");
    }

    @Test
    @DisplayName("replie sur le filtre brut si le format est inconnu")
    void repli_si_inconnu() {
        String inconnu = "blabla\nrien de structuré ici\n";
        // Ne doit pas planter ni perdre l'information : on retombe sur le texte brut.
        assertThat(AbstractTestChecker.extraitEchecs(inconnu)).isEqualTo(inconnu);
    }
}
