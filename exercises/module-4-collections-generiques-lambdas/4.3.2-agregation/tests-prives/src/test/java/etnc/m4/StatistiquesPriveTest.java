package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests prives de l'exercice 4.3.2 (cas limites : liste vide, singleton, valeurs egales).
 */
@DisplayName("Statistiques — tests prives")
class StatistiquesPriveTest {

    @Test
    @DisplayName("liste vide : ancienneteMoyenne renvoie 0.0 (sentinelle, pas d'exception)")
    void vide_moyenne_sentinelle() {
        assertThat(Statistiques.ancienneteMoyenne(List.of()))
                .isCloseTo(0.0, within(0.001));
    }

    @Test
    @DisplayName("liste vide : ancienneteMax renvoie 0 (sentinelle, pas d'exception)")
    void vide_max_sentinelle() {
        assertThat(Statistiques.ancienneteMax(List.of())).isEqualTo(0);
    }

    @Test
    @DisplayName("un seul element : ancienneteMoyenne egale son anciennete")
    void singleton_moyenne_egale_anciennete() {
        List<Soldat> un = List.of(new Soldat("Seul", Grade.ADJUDANT, 12));
        assertThat(Statistiques.ancienneteMoyenne(un))
                .isCloseTo(12.0, within(0.001));
    }

    @Test
    @DisplayName("valeurs egales : ancienneteMoyenne egale cette valeur")
    void valeurs_egales_moyenne_identique() {
        List<Soldat> pareils = List.of(
                new Soldat("Alpha", Grade.SOLDAT, 5),
                new Soldat("Beta", Grade.SOLDAT, 5),
                new Soldat("Gamma", Grade.SOLDAT, 5)
        );
        assertThat(Statistiques.ancienneteMoyenne(pareils))
                .isCloseTo(5.0, within(0.001));
    }
}
