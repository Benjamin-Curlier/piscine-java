package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.1.3 Soldat.
 */
@DisplayName("Soldat — tests publics")
class SoldatTest {

    @Test
    @DisplayName("construction et accesseurs")
    void construction() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        assertThat(s.getNom()).isEqualTo("Martin");
        assertThat(s.getGrade()).isEqualTo("Sergent");
        assertThat(s.getPointsDeVie()).isEqualTo(100);
    }

    @Test
    @DisplayName("subirDegats réduit les points de vie")
    void subir_degats() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        s.subirDegats(30);
        assertThat(s.getPointsDeVie()).isEqualTo(70);
    }

    @Test
    @DisplayName("soigner augmente les points de vie")
    void soigner() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        s.soigner(20);
        assertThat(s.getPointsDeVie()).isEqualTo(120);
    }

    @Test
    @DisplayName("estVivant est vrai au départ")
    void est_vivant() {
        Soldat s = new Soldat("Martin", "Sergent", 100);
        assertThat(s.estVivant()).isTrue();
    }

    @Test
    @DisplayName("toString au format attendu")
    void representation() {
        assertThat(new Soldat("Martin", "Sergent", 100))
            .hasToString("Martin (Sergent) - 100 PV");
    }
}
