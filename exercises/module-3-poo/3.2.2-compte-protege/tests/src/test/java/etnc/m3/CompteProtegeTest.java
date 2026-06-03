package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.2.2 CompteProtege.
 */
@DisplayName("CompteProtege — tests publics")
class CompteProtegeTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        CompteProtege c = new CompteProtege("Dupont", 100.0);
        assertThat(c.getTitulaire()).isEqualTo("Dupont");
        assertThat(c.getSolde()).isEqualTo(100.0, within(1e-9));
    }

    @Test
    @DisplayName("deposer crédite le solde")
    void deposer() {
        CompteProtege c = new CompteProtege("Dupont", 100.0);
        c.deposer(50.0);
        assertThat(c.getSolde()).isEqualTo(150.0, within(1e-9));
    }

    @Test
    @DisplayName("retirer débite le solde")
    void retirer() {
        CompteProtege c = new CompteProtege("Dupont", 100.0);
        c.retirer(30.0);
        assertThat(c.getSolde()).isEqualTo(70.0, within(1e-9));
    }

    @Test
    @DisplayName("toString au format attendu")
    void representation() {
        assertThat(new CompteProtege("Dupont", 100.0))
            .hasToString("Compte de Dupont : 100.00 €");
    }
}
