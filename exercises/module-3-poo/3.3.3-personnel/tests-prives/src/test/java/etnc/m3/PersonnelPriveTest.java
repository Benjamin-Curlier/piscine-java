package etnc.m3;

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
        Personnel p = new MilitaireDuRang("Bernard");
        assertThat(p.fiche()).isEqualTo("Bernard — Militaire du rang — 1600.00 €");
    }

    @Test
    @DisplayName("getNom hérité")
    void getter_herite() {
        assertThat(new Officier("Durand", 1).getNom()).isEqualTo("Durand");
    }

    @Test
    @DisplayName("officier échelon 0")
    void officier_echelon_zero() {
        assertThat(new Officier("Durand", 0).solde()).isEqualTo(3000.0, within(1e-9));
    }

    @Test
    @DisplayName("tableau hétérogène de personnels")
    void tableau() {
        Personnel[] section = {
            new Officier("Durand", 2),
            new SousOfficier("Petit", 4),
            new MilitaireDuRang("Bernard")
        };
        double total = 0;
        for (Personnel p : section) {
            total += p.solde();
        }
        // 3400 + 2600 + 1600
        assertThat(total).isEqualTo(7600.0, within(1e-9));
    }
}
