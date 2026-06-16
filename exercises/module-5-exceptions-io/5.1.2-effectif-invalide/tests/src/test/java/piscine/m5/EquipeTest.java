package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests publics de l'exercice 5.1.2 (effectif invalide — exception personnalisée).
 */
@DisplayName("Equipe — tests publics")
class EquipeTest {

    @Test
    @DisplayName("affecter(0) est valide et l'effectif vaut 0")
    void affecter_zero_valide() {
        Equipe equipe = new Equipe("Alpha", 100);
        assertThatCode(() -> equipe.affecter(0)).doesNotThrowAnyException();
        assertThat(equipe.getEffectif()).isEqualTo(0);
    }

    @Test
    @DisplayName("affecter une valeur au milieu de la plage met a jour l'effectif")
    void affecter_milieu_met_a_jour() {
        Equipe equipe = new Equipe("Alpha", 100);
        equipe.affecter(50);
        assertThat(equipe.getEffectif()).isEqualTo(50);
    }

    @Test
    @DisplayName("affecter capaciteMax est valide et l'effectif reflete la valeur")
    void affecter_capacite_max_valide() {
        Equipe equipe = new Equipe("Alpha", 100);
        equipe.affecter(100);
        assertThat(equipe.getEffectif()).isEqualTo(100);
    }

    @Test
    @DisplayName("affecter(-1) leve une EffectifInvalideException")
    void affecter_negatif_leve() {
        Equipe equipe = new Equipe("Alpha", 100);
        assertThatThrownBy(() -> equipe.affecter(-1))
            .isInstanceOf(EffectifInvalideException.class);
    }

    @Test
    @DisplayName("affecter au-dela de la capacite leve une EffectifInvalideException")
    void affecter_au_dela_capacite_leve() {
        Equipe equipe = new Equipe("Alpha", 100);
        assertThatThrownBy(() -> equipe.affecter(101))
            .isInstanceOf(EffectifInvalideException.class);
    }

    @Test
    @DisplayName("le message de l'exception contient la valeur fautive")
    void message_contient_valeur_fautive() {
        Equipe equipe = new Equipe("Alpha", 100);
        assertThatThrownBy(() -> equipe.affecter(150))
            .isInstanceOf(EffectifInvalideException.class)
            .hasMessageContaining("150");
    }

    @Test
    @DisplayName("affecter un texte valide met a jour l'effectif")
    void affecter_texte_valide() {
        Equipe equipe = new Equipe("Alpha", 100);
        equipe.affecter("30");
        assertThat(equipe.getEffectif()).isEqualTo(30);
    }

    @Test
    @DisplayName("affecter un texte illisible leve une EffectifInvalideException")
    void affecter_texte_illisible_leve() {
        Equipe equipe = new Equipe("Alpha", 100);
        assertThatThrownBy(() -> equipe.affecter("abc"))
            .isInstanceOf(EffectifInvalideException.class);
    }
}
