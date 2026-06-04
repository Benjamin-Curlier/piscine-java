package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.3.3 (collecteurs groupingBy / partitioningBy / counting).
 */
@DisplayName("Regroupement — tests publics")
class RegroupementTest {

    private static final Soldat MARTIN = new Soldat("Martin", Grade.SERGENT, 8);
    private static final Soldat DURAND = new Soldat("Durand", Grade.CAPORAL, 3);
    private static final Soldat PETIT  = new Soldat("Petit",  Grade.SERGENT, 12);
    private static final Soldat MORA   = new Soldat("Mora",   Grade.ADJUDANT, 15);

    private static final List<Soldat> LISTE = List.of(MARTIN, DURAND, PETIT, MORA);

    @Test
    @DisplayName("parGrade regroupe les soldats par grade")
    void par_grade_contient_les_sergents() {
        Map<Grade, List<Soldat>> carte = Regroupement.parGrade(LISTE);
        assertThat(carte.get(Grade.SERGENT))
                .containsExactlyInAnyOrder(MARTIN, PETIT);
    }

    @Test
    @DisplayName("selonAnciennete partitionne correctement selon le seuil")
    void selon_anciennete_partitions_correctes() {
        Map<Boolean, List<Soldat>> carte = Regroupement.selonAnciennete(LISTE, 10);
        assertThat(carte.get(true)).containsExactlyInAnyOrder(PETIT, MORA);
        assertThat(carte.get(false)).containsExactlyInAnyOrder(MARTIN, DURAND);
    }

    @Test
    @DisplayName("effectifsParGrade compte le nombre de soldats par grade")
    void effectifs_par_grade() {
        Map<Grade, Long> carte = Regroupement.effectifsParGrade(LISTE);
        assertThat(carte.get(Grade.SERGENT)).isEqualTo(2L);
        assertThat(carte.get(Grade.CAPORAL)).isEqualTo(1L);
        assertThat(carte.get(Grade.ADJUDANT)).isEqualTo(1L);
    }
}
