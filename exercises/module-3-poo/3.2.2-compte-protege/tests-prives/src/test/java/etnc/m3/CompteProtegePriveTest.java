package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.2.2 CompteProtege — gardes.
 */
@DisplayName("CompteProtege — tests privés")
class CompteProtegePriveTest {

    @Test
    @DisplayName("solde initial négatif corrigé à 0.0")
    void solde_initial_corrige() {
        CompteProtege c = new CompteProtege("Marie", -50.0);
        assertThat(c.getSolde()).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("deposer un montant <= 0 est ignoré")
    void deposer_invalide() {
        CompteProtege c = new CompteProtege("Marie", 100.0);
        c.deposer(-10.0);
        c.deposer(0.0);
        assertThat(c.getSolde()).isEqualTo(100.0, within(1e-9));
    }

    @Test
    @DisplayName("retirer plus que le solde est refusé (inchangé)")
    void retirer_trop_grand() {
        CompteProtege c = new CompteProtege("Marie", 50.0);
        c.retirer(80.0);
        assertThat(c.getSolde()).isEqualTo(50.0, within(1e-9));
    }

    @Test
    @DisplayName("retirer exactement le solde amène à 0.0")
    void retirer_tout() {
        CompteProtege c = new CompteProtege("Marie", 50.0);
        c.retirer(50.0);
        assertThat(c.getSolde()).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("retirer un montant <= 0 est ignoré")
    void retirer_invalide() {
        CompteProtege c = new CompteProtege("Marie", 100.0);
        c.retirer(-10.0);
        c.retirer(0.0);
        assertThat(c.getSolde()).isEqualTo(100.0, within(1e-9));
    }
}
