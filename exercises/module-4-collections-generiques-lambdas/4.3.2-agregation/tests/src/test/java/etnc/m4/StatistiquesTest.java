package etnc.m4;

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

    private static final List<Soldat> TROUPE = List.of(
            new Soldat("Dupont", Grade.CAPORAL, 3),
            new Soldat("Martin", Grade.SERGENT, 7),
            new Soldat("Legrand", Grade.SOLDAT, 1)
    );

    @Test
    @DisplayName("total renvoie le nombre de soldats")
    void total_nombre_elements() {
        assertThat(Statistiques.total(TROUPE)).isEqualTo(3);
    }

    @Test
    @DisplayName("ancienneteMoyenne correcte a 0.001 pres")
    void anciennete_moyenne() {
        // (3 + 7 + 1) / 3 = 3.666...
        assertThat(Statistiques.ancienneteMoyenne(TROUPE))
                .isCloseTo(11.0 / 3.0, within(0.001));
    }

    @Test
    @DisplayName("ancienneteMax renvoie la plus grande anciennete")
    void anciennete_max() {
        assertThat(Statistiques.ancienneteMax(TROUPE)).isEqualTo(7);
    }
}
