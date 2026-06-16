package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 4.3.2 (reductions numeriques).
 */
@DisplayName("Statistiques — tests publics")
class StatistiquesTest {

    private static final List<Membre> EQUIPE = List.of(
            new Membre("Dupont", Niveau.CONFIRME, 3),
            new Membre("Martin", Niveau.SENIOR, 7),
            new Membre("Legrand", Niveau.JUNIOR, 1)
    );

    @Test
    @DisplayName("total renvoie le nombre de membres")
    void total_nombre_elements() {
        assertThat(Statistiques.total(EQUIPE)).isEqualTo(3);
    }

    @Test
    @DisplayName("ancienneteMoyenne correcte a 0.001 pres")
    void anciennete_moyenne() {
        // (3 + 7 + 1) / 3 = 3.666...
        assertThat(Statistiques.ancienneteMoyenne(EQUIPE))
                .isCloseTo(11.0 / 3.0, within(0.001));
    }

    @Test
    @DisplayName("ancienneteMax renvoie la plus grande anciennete")
    void anciennete_max() {
        assertThat(Statistiques.ancienneteMax(EQUIPE)).isEqualTo(7);
    }
}
