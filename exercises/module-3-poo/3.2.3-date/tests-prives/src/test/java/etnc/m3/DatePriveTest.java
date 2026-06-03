package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.2.3 Date — bornage.
 */
@DisplayName("Date — tests privés")
class DatePriveTest {

    @Test
    @DisplayName("jour trop petit borné à 1")
    void jour_trop_petit() {
        Date d = new Date(0, 3, 2026);
        assertThat(d.getJour()).isEqualTo(1);
    }

    @Test
    @DisplayName("jour trop grand borné à 31")
    void jour_trop_grand() {
        Date d = new Date(40, 3, 2026);
        assertThat(d.getJour()).isEqualTo(31);
    }

    @Test
    @DisplayName("mois trop grand borné à 12")
    void mois_trop_grand() {
        Date d = new Date(7, 13, 2026);
        assertThat(d.getMois()).isEqualTo(12);
    }

    @Test
    @DisplayName("setMois hors plage : ignoré (inchangé)")
    void set_mois_refuse() {
        Date d = new Date(7, 3, 2026);
        d.setMois(0);
        assertThat(d.getMois()).isEqualTo(3);
        d.setMois(13);
        assertThat(d.getMois()).isEqualTo(3);
    }

    @Test
    @DisplayName("toString zéro-padding sur jour et mois à un chiffre")
    void toString_padding() {
        assertThat(new Date(5, 9, 2026)).hasToString("05/09/2026");
    }
}
