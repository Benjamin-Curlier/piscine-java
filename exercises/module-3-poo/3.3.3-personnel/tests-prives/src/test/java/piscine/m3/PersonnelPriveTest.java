package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.3.3 Personnel — polymorphisme.
 */
@DisplayName("Personnel — tests privés")
class PersonnelPriveTest {

    @Test
    @DisplayName("fiche via le type de base")
    void fiche_polymorphe() {
        Personnel p = new Junior("Bernard");
        assertThat(p.fiche()).isEqualTo("Bernard — Junior — 1600.00 €");
    }

    @Test
    @DisplayName("getNom hérité")
    void getter_herite() {
        assertThat(new Senior("Durand", 1).getNom()).isEqualTo("Durand");
    }

    @Test
    @DisplayName("senior échelon 0")
    void senior_echelon_zero() {
        assertThat(new Senior("Durand", 0).solde()).isEqualTo(3000.0, within(1e-9));
    }

    @Test
    @DisplayName("tableau hétérogène de personnels")
    void tableau() {
        Personnel[] equipe = {
            new Senior("Durand", 2),
            new Confirme("Petit", 4),
            new Junior("Bernard")
        };
        double total = 0;
        for (Personnel p : equipe) {
            total += p.solde();
        }
        // 3400 + 2600 + 1600
        assertThat(total).isEqualTo(7600.0, within(1e-9));
    }
}
