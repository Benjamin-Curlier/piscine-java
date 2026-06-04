package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.3.1 (cas limites filtrage et projection).
 */
@DisplayName("Effectifs — tests prives")
class EffectifsPriveTest {

    @Test
    @DisplayName("filtrer : aucun element ne satisfait -> liste vide (pas null)")
    void filtrer_aucun_element_satisfait() {
        List<Soldat> troupe = List.of(
                new Soldat("Dupont", Grade.SOLDAT, 1),
                new Soldat("Martin", Grade.CAPORAL, 2)
        );
        List<Soldat> resultat = Effectifs.filtrer(troupe, s -> s.grade() == Grade.LIEUTENANT);
        assertThat(resultat).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("filtrer : tous les elements satisfont -> tous renvoyes")
    void filtrer_tous_satisfont() {
        List<Soldat> troupe = List.of(
                new Soldat("Alpha",  Grade.ADJUDANT,  10),
                new Soldat("Bravo",  Grade.LIEUTENANT, 12),
                new Soldat("Charlie", Grade.SERGENT,   7)
        );
        List<Soldat> resultat = Effectifs.filtrer(troupe, s -> s.anciennete() > 5);
        assertThat(resultat).containsExactlyElementsOf(troupe);
    }

    @Test
    @DisplayName("filtrer : predicate toujours faux -> liste vide")
    void filtrer_predicate_toujours_faux() {
        List<Soldat> troupe = List.of(
                new Soldat("Dupont", Grade.SOLDAT,  3),
                new Soldat("Martin", Grade.CAPORAL, 5)
        );
        List<Soldat> resultat = Effectifs.filtrer(troupe, s -> false);
        assertThat(resultat).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("filtrer : la liste source n'est pas modifiee")
    void filtrer_source_non_modifiee() {
        List<Soldat> source = new ArrayList<>();
        source.add(new Soldat("Dupont", Grade.SOLDAT,    2));
        source.add(new Soldat("Martin", Grade.LIEUTENANT, 9));
        int tailleAvant = source.size();

        Effectifs.filtrer(source, s -> s.grade() == Grade.LIEUTENANT);

        assertThat(source).hasSize(tailleAvant);
    }
}
