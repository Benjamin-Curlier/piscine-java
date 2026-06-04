package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests publics de l'exercice 5.1.1 (validation défensive d'une saisie entière).
 */
@DisplayName("ValidationSaisie — tests publics")
class ValidationSaisieTest {

    @Test
    @DisplayName("valeur au milieu de [0,10] est renvoyée telle quelle")
    void valeur_milieu_renvoyee() {
        assertThat(ValidationSaisie.validerEntier("5", 0, 10)).isEqualTo(5);
    }

    @Test
    @DisplayName("valeur egale au minimum est acceptee")
    void valeur_egale_au_minimum() {
        assertThat(ValidationSaisie.validerEntier("0", 0, 10)).isEqualTo(0);
    }

    @Test
    @DisplayName("valeur egale au maximum est acceptee")
    void valeur_egale_au_maximum() {
        assertThat(ValidationSaisie.validerEntier("10", 0, 10)).isEqualTo(10);
    }

    @Test
    @DisplayName("valeur sous le minimum leve IllegalArgumentException")
    void valeur_sous_minimum_leve_iae() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("-1", 0, 10))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("valeur au-dessus du maximum leve IllegalArgumentException")
    void valeur_dessus_maximum_leve_iae() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("11", 0, 10))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("texte non numerique leve NumberFormatException (type precis)")
    void texte_non_numerique_leve_nfe() {
        // NumberFormatException extends IllegalArgumentException —
        // on asserte le type PRÉCIS pour distinguer illisible de hors-plage.
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("abc", 0, 10))
            .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("message de l'exception hors-plage contient les bornes 0 et 10")
    void message_iae_contient_bornes() {
        assertThatThrownBy(() -> ValidationSaisie.validerEntier("-1", 0, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("0")
            .hasMessageContaining("10");
    }

    @Test
    @DisplayName("cas valide ne leve aucune exception")
    void cas_valide_ne_leve_pas() {
        assertThatCode(() -> ValidationSaisie.validerEntier("7", 0, 10))
            .doesNotThrowAnyException();
    }
}
