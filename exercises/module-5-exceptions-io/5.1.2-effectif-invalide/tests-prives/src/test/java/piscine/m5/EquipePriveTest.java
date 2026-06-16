package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests privés de l'exercice 5.1.2 (effectif invalide — cas limites).
 */
@DisplayName("Equipe — tests privés")
class EquipePriveTest {

    @Test
    @DisplayName("EffectifInvalideException est unchecked (hérite de RuntimeException)")
    void exception_est_unchecked() {
        EffectifInvalideException exception = new EffectifInvalideException("x");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("bornes exactes : -1 et 101 invalides, 0 et 100 valides")
    void bornes_exactes() {
        Equipe equipe = new Equipe("Bravo", 100);
        assertThatThrownBy(() -> equipe.affecter(-1))
            .isInstanceOf(EffectifInvalideException.class);
        assertThatThrownBy(() -> equipe.affecter(101))
            .isInstanceOf(EffectifInvalideException.class);
        assertThatCode(() -> equipe.affecter(0)).doesNotThrowAnyException();
        assertThatCode(() -> equipe.affecter(100)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("affecter un texte illisible chaîne la NumberFormatException d'origine")
    void texte_illisible_chaine_la_cause() {
        Equipe equipe = new Equipe("Charlie", 100);
        assertThatThrownBy(() -> equipe.affecter("abc"))
            .isInstanceOf(EffectifInvalideException.class)
            .hasCauseInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("affecter un texte parseable hors plage leve sans cause (getCause() == null)")
    void texte_parseable_hors_plage_sans_cause() {
        Equipe equipe = new Equipe("Delta", 100);
        assertThatThrownBy(() -> equipe.affecter("-5"))
            .isInstanceOf(EffectifInvalideException.class)
            .hasNoCause();
    }

    @Test
    @DisplayName("le message de l'exception de plage contient le nom de l'unité")
    void message_contient_nom_equipe() {
        Equipe equipe = new Equipe("Alpha", 10);
        assertThatThrownBy(() -> equipe.affecter(99))
            .isInstanceOf(EffectifInvalideException.class)
            .hasMessageContaining("Alpha");
    }

    @Test
    @DisplayName("capaciteMax == 0 : seul 0 est valide, 1 est invalide")
    void capacite_nulle() {
        Equipe equipe = new Equipe("Echo", 0);
        assertThatCode(() -> equipe.affecter(0)).doesNotThrowAnyException();
        assertThat(equipe.getEffectif()).isEqualTo(0);
        assertThatThrownBy(() -> equipe.affecter(1))
            .isInstanceOf(EffectifInvalideException.class);
    }

    @Test
    @DisplayName("l'état reste inchangé après une affectation qui échoue")
    void etat_inchange_apres_echec() {
        Equipe equipe = new Equipe("Foxtrot", 100);
        equipe.affecter(50);
        assertThatThrownBy(() -> equipe.affecter(999))
            .isInstanceOf(EffectifInvalideException.class);
        // La levée ne doit pas avoir corrompu l'effectif : il reste à la dernière valeur valide.
        assertThat(equipe.getEffectif()).isEqualTo(50);
    }

    @Test
    @DisplayName("affecter un texte à la borne haute (\"100\") est valide")
    void texte_a_la_borne_haute() {
        Equipe equipe = new Equipe("Golf", 100);
        equipe.affecter("100");
        assertThat(equipe.getEffectif()).isEqualTo(100);
    }
}
