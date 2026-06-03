package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.2.3 Date.
 */
@DisplayName("Date — tests publics")
class DateTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        Date d = new Date(7, 3, 2026);
        assertThat(d.getJour()).isEqualTo(7);
        assertThat(d.getMois()).isEqualTo(3);
        assertThat(d.getAnnee()).isEqualTo(2026);
    }

    @Test
    @DisplayName("toString au format JJ/MM/AAAA")
    void representation() {
        assertThat(new Date(7, 3, 2026)).hasToString("07/03/2026");
    }

    @Test
    @DisplayName("setJour avec une valeur valide")
    void set_jour_valide() {
        Date d = new Date(7, 3, 2026);
        d.setJour(15);
        assertThat(d.getJour()).isEqualTo(15);
    }
}
