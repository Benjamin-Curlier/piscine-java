package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.4.2 Grade.
 */
@DisplayName("Grade — tests privés")
class GradePriveTest {

    @Test
    @DisplayName("solde de base du lieutenant")
    void solde_lieutenant() {
        assertThat(Grade.LIEUTENANT.getSoldeBase()).isEqualTo(3000.0, within(1e-9));
    }

    @Test
    @DisplayName("categorie des grades intermédiaires")
    void categorie_intermediaire() {
        assertThat(Grade.CAPORAL.categorie()).isEqualTo("Militaire du rang");
        assertThat(Grade.ADJUDANT.categorie()).isEqualTo("Sous-officier");
    }

    @Test
    @DisplayName("valueOf retrouve la constante")
    void value_of() {
        assertThat(Grade.valueOf("SERGENT")).isEqualTo(Grade.SERGENT);
    }
}
