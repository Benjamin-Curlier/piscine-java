package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests privés de l'exercice 5.1.1 (cas limites et comportements fins).
 */
@DisplayName("ValidationSaisie — tests prives")
class ValidationSaisiePriveTest {

    @Test
    @DisplayName("min-1 est refuse, min est accepte (borne inferieure exacte)")
    void borne_inferieure_exacte() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("-1", 0, 10))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> ValidationSaisie.validerEntier("0", 0, 10))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("max+1 est refuse, max est accepte (borne superieure exacte)")
    void borne_superieure_exacte() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("11", 0, 10))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> ValidationSaisie.validerEntier("10", 0, 10))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("plage d'une seule valeur : min==max accepte, toute autre valeur refusee")
    void plage_une_seule_valeur() {
        assertThat(ValidationSaisie.validerEntier("5", 5, 5)).isEqualTo(5);
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("4", 5, 5))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("6", 5, 5))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("plage negative [-10,-1] : -5 est accepte, 0 est refuse")
    void plage_negative() {
        assertThat(ValidationSaisie.validerEntier("-5", -10, -1)).isEqualTo(-5);
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("0", -10, -1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("trim : espaces autour du chiffre sont ignores")
    void trim_espaces_ignores() {
        assertThat(ValidationSaisie.validerEntier("  7  ", 0, 10)).isEqualTo(7);
    }

    @Test
    @DisplayName("signe explicite positif : +5 est accepte dans [0,10]")
    void signe_explicite_positif() {
        assertThat(ValidationSaisie.validerEntier("+5", 0, 10)).isEqualTo(5);
    }

    @Test
    @DisplayName("texte null leve NumberFormatException (pas NullPointerException)")
    void null_leve_nfe_pas_npe() {
        // La garde null doit être explicite AVANT texte.trim() pour éviter une NPE
        // sans message utile ; on regroupe null avec les entrées illisibles.
        assertThatThrownBy(() -> ValidationSaisie.validerEntier(null, 0, 10))
            .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("overflow (valeur > Integer.MAX_VALUE) leve NumberFormatException")
    void overflow_leve_nfe() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("99999999999", 0, 100))
            .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("message de l'IAE hors-plage contient la valeur fautive")
    void message_iae_contient_valeur_fautive() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("42", 0, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("42");
    }
}
