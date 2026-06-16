package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.1.2 Compte — cas limites.
 */
@DisplayName("Compte — tests privés")
class ComptePriveTest {

    @Test
    @DisplayName("enchaînement dépôt puis retrait")
    void enchainement() {
        Compte c = new Compte("Marie", 100.0);
        c.deposer(50.0);
        c.retirer(30.0);
        assertThat(c.getSolde()).isEqualTo(120.0, within(1e-9));
    }

    @Test
    @DisplayName("retrait autorisé même si le solde devient négatif (pas de garde en 3.1)")
    void solde_negatif() {
        Compte c = new Compte("Marie", 50.0);
        c.retirer(80.0);
        assertThat(c.getSolde()).isEqualTo(-30.0, within(1e-9));
    }

    @Test
    @DisplayName("solde initial à zéro")
    void solde_initial_zero() {
        Compte c = new Compte("Marie", 0.0);
        assertThat(c.getSolde()).isEqualTo(0.0, within(1e-9));
    }

    @Test
    @DisplayName("toString affiche deux décimales")
    void toString_deux_decimales() {
        assertThat(new Compte("Marie", 12.5))
            .hasToString("Compte de Marie : 12.50 €");
    }
}
