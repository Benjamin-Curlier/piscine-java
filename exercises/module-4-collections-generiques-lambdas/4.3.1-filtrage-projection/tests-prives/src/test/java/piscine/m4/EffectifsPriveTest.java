package piscine.m4;

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
        List<Membre> equipe = List.of(
                new Membre("Dupont", Niveau.JUNIOR, 1),
                new Membre("Martin", Niveau.CONFIRME, 2)
        );
        List<Membre> resultat = Effectifs.filtrer(equipe, s -> s.niveau() == Niveau.PRINCIPAL);
        assertThat(resultat).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("filtrer : tous les elements satisfont -> tous renvoyes")
    void filtrer_tous_satisfont() {
        List<Membre> equipe = List.of(
                new Membre("Alpha",  Niveau.LEAD,  10),
                new Membre("Bravo",  Niveau.PRINCIPAL, 12),
                new Membre("Charlie", Niveau.SENIOR,   7)
        );
        List<Membre> resultat = Effectifs.filtrer(equipe, s -> s.anciennete() > 5);
        assertThat(resultat).containsExactlyElementsOf(equipe);
    }

    @Test
    @DisplayName("filtrer : predicate toujours faux -> liste vide")
    void filtrer_predicate_toujours_faux() {
        List<Membre> equipe = List.of(
                new Membre("Dupont", Niveau.JUNIOR,  3),
                new Membre("Martin", Niveau.CONFIRME, 5)
        );
        List<Membre> resultat = Effectifs.filtrer(equipe, s -> false);
        assertThat(resultat).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("filtrer : la liste source n'est pas modifiee")
    void filtrer_source_non_modifiee() {
        List<Membre> source = new ArrayList<>();
        source.add(new Membre("Dupont", Niveau.JUNIOR,    2));
        source.add(new Membre("Martin", Niveau.PRINCIPAL, 9));
        int tailleAvant = source.size();

        Effectifs.filtrer(source, s -> s.niveau() == Niveau.PRINCIPAL);

        assertThat(source).hasSize(tailleAvant);
    }
}
