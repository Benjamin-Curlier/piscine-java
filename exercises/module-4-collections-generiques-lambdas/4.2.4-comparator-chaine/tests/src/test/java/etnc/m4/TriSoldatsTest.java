package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.4 (chaînage de Comparator).
 */
@DisplayName("TriSoldats — tests publics")
class TriSoldatsTest {

    private static final Soldat ALPHA   = new Soldat("Alpha",   Grade.SERGENT,    3);
    private static final Soldat BRAVO   = new Soldat("Bravo",   Grade.LIEUTENANT, 7);
    private static final Soldat CHARLIE = new Soldat("Charlie", Grade.CAPORAL,    1);
    private static final Soldat DELTA   = new Soldat("Delta",   Grade.SOLDAT,     2);

    @Test
    @DisplayName("parNom trie par ordre alphabétique")
    void par_nom() {
        List<Soldat> soldats = List.of(DELTA, BRAVO, ALPHA, CHARLIE);
        List<Soldat> resultat = TriSoldats.parNom(soldats);
        assertThat(resultat).containsExactly(ALPHA, BRAVO, CHARLIE, DELTA);
    }

    @Test
    @DisplayName("parGradePuisNom trie par grade croissant puis par nom")
    void par_grade_puis_nom() {
        List<Soldat> soldats = List.of(ALPHA, BRAVO, CHARLIE, DELTA);
        List<Soldat> resultat = TriSoldats.parGradePuisNom(soldats);
        // SOLDAT < CAPORAL < SERGENT < LIEUTENANT
        assertThat(resultat).containsExactly(DELTA, CHARLIE, ALPHA, BRAVO);
    }

    @Test
    @DisplayName("parGradeDecroissant trie par grade décroissant")
    void par_grade_decroissant() {
        List<Soldat> soldats = List.of(DELTA, CHARLIE, ALPHA, BRAVO);
        List<Soldat> resultat = TriSoldats.parGradeDecroissant(soldats);
        // LIEUTENANT > SERGENT > CAPORAL > SOLDAT
        assertThat(resultat).containsExactly(BRAVO, ALPHA, CHARLIE, DELTA);
    }
}
