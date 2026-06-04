package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests publics de l'exercice 5.1.2 (effectif invalide — exception personnalisée).
 */
@DisplayName("Unite — tests publics")
class UniteTest {

    @Test
    @DisplayName("affecter(0) est valide et l'effectif vaut 0")
    void affecter_zero_valide() {
        Unite unite = new Unite("Alpha", 100);
        assertThatCode(() -> unite.affecter(0)).doesNotThrowAnyException();
        assertThat(unite.getEffectif()).isEqualTo(0);
    }

    @Test
    @DisplayName("affecter une valeur au milieu de la plage met a jour l'effectif")
    void affecter_milieu_met_a_jour() {
        Unite unite = new Unite("Alpha", 100);
        unite.affecter(50);
        assertThat(unite.getEffectif()).isEqualTo(50);
    }

    @Test
    @DisplayName("affecter capaciteMax est valide et l'effectif reflete la valeur")
    void affecter_capacite_max_valide() {
        Unite unite = new Unite("Alpha", 100);
        unite.affecter(100);
        assertThat(unite.getEffectif()).isEqualTo(100);
    }

    @Test
    @DisplayName("affecter(-1) leve une EffectifInvalideException")
    void affecter_negatif_leve() {
        Unite unite = new Unite("Alpha", 100);
        assertThatThrownBy(() -> unite.affecter(-1))
            .isInstanceOf(EffectifInvalideException.class);
    }

    @Test
    @DisplayName("affecter au-dela de la capacite leve une EffectifInvalideException")
    void affecter_au_dela_capacite_leve() {
        Unite unite = new Unite("Alpha", 100);
        assertThatThrownBy(() -> unite.affecter(101))
            .isInstanceOf(EffectifInvalideException.class);
    }

    @Test
    @DisplayName("le message de l'exception contient la valeur fautive")
    void message_contient_valeur_fautive() {
        Unite unite = new Unite("Alpha", 100);
        assertThatThrownBy(() -> unite.affecter(150))
            .isInstanceOf(EffectifInvalideException.class)
            .hasMessageContaining("150");
    }

    @Test
    @DisplayName("affecter un texte valide met a jour l'effectif")
    void affecter_texte_valide() {
        Unite unite = new Unite("Alpha", 100);
        unite.affecter("30");
        assertThat(unite.getEffectif()).isEqualTo(30);
    }

    @Test
    @DisplayName("affecter un texte illisible leve une EffectifInvalideException")
    void affecter_texte_illisible_leve() {
        Unite unite = new Unite("Alpha", 100);
        assertThatThrownBy(() -> unite.affecter("abc"))
            .isInstanceOf(EffectifInvalideException.class);
    }
}
