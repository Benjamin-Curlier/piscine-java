package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.3.3 Personnel.
 */
@DisplayName("Personnel — tests publics")
class PersonnelTest {

    @Test
    @DisplayName("Officier : grade et solde")
    void officier() {
        Officier o = new Officier("Durand", 3);
        assertThat(o.grade()).isEqualTo("Officier");
        assertThat(o.solde()).isEqualTo(3600.0, within(1e-9));
    }

    @Test
    @DisplayName("Sous-officier : solde")
    void sous_officier() {
        assertThat(new SousOfficier("Petit", 5).solde()).isEqualTo(2700.0, within(1e-9));
    }

    @Test
    @DisplayName("Militaire du rang : grade et solde")
    void militaire_du_rang() {
        MilitaireDuRang m = new MilitaireDuRang("Bernard");
        assertThat(m.grade()).isEqualTo("Militaire du rang");
        assertThat(m.solde()).isEqualTo(1600.0, within(1e-9));
    }

    @Test
    @DisplayName("fiche d'un officier")
    void fiche() {
        assertThat(new Officier("Durand", 3).fiche())
            .isEqualTo("Durand — Officier — 3600.00 €");
    }
}
