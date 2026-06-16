package piscine.m4;

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

    private static final Membre MARTIN = new Membre("Martin", Niveau.SENIOR, 8);
    private static final Membre DURAND = new Membre("Durand", Niveau.CONFIRME, 3);
    private static final Membre PETIT  = new Membre("Petit",  Niveau.SENIOR, 12);
    private static final Membre MORA   = new Membre("Mora",   Niveau.LEAD, 15);

    private static final List<Membre> LISTE = List.of(MARTIN, DURAND, PETIT, MORA);

    @Test
    @DisplayName("parNiveau regroupe les membres par niveau")
    void par_niveau_contient_les_seniors() {
        Map<Niveau, List<Membre>> carte = Regroupement.parNiveau(LISTE);
        assertThat(carte.get(Niveau.SENIOR))
                .containsExactlyInAnyOrder(MARTIN, PETIT);
    }

    @Test
    @DisplayName("selonAnciennete partitionne correctement selon le seuil")
    void selon_anciennete_partitions_correctes() {
        Map<Boolean, List<Membre>> carte = Regroupement.selonAnciennete(LISTE, 10);
        assertThat(carte.get(true)).containsExactlyInAnyOrder(PETIT, MORA);
        assertThat(carte.get(false)).containsExactlyInAnyOrder(MARTIN, DURAND);
    }

    @Test
    @DisplayName("effectifsParNiveau compte le nombre de membres par niveau")
    void effectifs_par_niveau() {
        Map<Niveau, Long> carte = Regroupement.effectifsParNiveau(LISTE);
        assertThat(carte.get(Niveau.SENIOR)).isEqualTo(2L);
        assertThat(carte.get(Niveau.CONFIRME)).isEqualTo(1L);
        assertThat(carte.get(Niveau.LEAD)).isEqualTo(1L);
    }
}
