package piscine.m3;

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
    @DisplayName("Senior : niveau et solde")
    void senior() {
        Senior o = new Senior("Durand", 3);
        assertThat(o.niveau()).isEqualTo("Senior");
        assertThat(o.solde()).isEqualTo(3600.0, within(1e-9));
    }

    @Test
    @DisplayName("Confirmé : solde")
    void confirme() {
        assertThat(new Confirme("Petit", 5).solde()).isEqualTo(2700.0, within(1e-9));
    }

    @Test
    @DisplayName("Junior : niveau et solde")
    void junior() {
        Junior m = new Junior("Bernard");
        assertThat(m.niveau()).isEqualTo("Junior");
        assertThat(m.solde()).isEqualTo(1600.0, within(1e-9));
    }

    @Test
    @DisplayName("fiche d'un senior")
    void fiche() {
        assertThat(new Senior("Durand", 3).fiche())
            .isEqualTo("Durand — Senior — 3600.00 €");
    }
}
