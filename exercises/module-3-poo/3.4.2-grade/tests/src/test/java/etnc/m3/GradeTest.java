package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.4.2 Grade.
 */
@DisplayName("Grade — tests publics")
class GradeTest {

    @Test
    @DisplayName("solde de base d'une constante")
    void solde_base() {
        assertThat(Grade.SOLDAT.getSoldeBase()).isEqualTo(1600.0, within(1e-9));
    }

    @Test
    @DisplayName("categorie selon la constante")
    void categorie() {
        assertThat(Grade.SOLDAT.categorie()).isEqualTo("Militaire du rang");
        assertThat(Grade.SERGENT.categorie()).isEqualTo("Sous-officier");
        assertThat(Grade.LIEUTENANT.categorie()).isEqualTo("Officier");
    }

    @Test
    @DisplayName("nombre de constantes")
    void valeurs() {
        assertThat(Grade.values()).hasSize(5);
    }
}
