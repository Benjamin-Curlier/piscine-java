package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 4.2.4.
 */
@DisplayName("TriSoldats — tests privés")
class TriSoldatsPriveTest {

    @Test
    @DisplayName("parGradePuisNom départage deux soldats de même grade par le nom")
    void chaine_meme_grade_departage_par_nom() {
        Soldat abel  = new Soldat("Abel",  Grade.SERGENT, 5);
        Soldat zorak = new Soldat("Zorak", Grade.SERGENT, 3);
        List<Soldat> soldats = List.of(zorak, abel);
        List<Soldat> resultat = TriSoldats.parGradePuisNom(soldats);
        assertThat(resultat).containsExactly(abel, zorak);
    }

    @Test
    @DisplayName("la liste source n'est pas mutée après parNom")
    void source_non_mutee() {
        Soldat delta = new Soldat("Delta", Grade.SOLDAT,     2);
        Soldat alpha = new Soldat("Alpha", Grade.LIEUTENANT, 9);
        List<Soldat> source = new ArrayList<>(List.of(delta, alpha));
        TriSoldats.parNom(source);
        assertThat(source).containsExactly(delta, alpha);
    }

    @Test
    @DisplayName("liste à un élément renvoyée inchangée")
    void liste_un_element() {
        Soldat seul = new Soldat("Seul", Grade.CAPORAL, 1);
        List<Soldat> source = List.of(seul);
        assertThat(TriSoldats.parNom(source)).containsExactly(seul);
        assertThat(TriSoldats.parGradePuisNom(source)).containsExactly(seul);
        assertThat(TriSoldats.parGradeDecroissant(source)).containsExactly(seul);
    }
}
