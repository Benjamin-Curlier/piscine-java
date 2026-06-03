package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.4.4 Evenement.
 */
@DisplayName("Evenement — tests publics")
class EvenementTest {

    @Test
    @DisplayName("resumer d'une connexion")
    void connexion() {
        assertThat(Journal.resumer(new Connexion("alice"))).isEqualTo("Connexion de alice");
    }

    @Test
    @DisplayName("resumer d'une deconnexion")
    void deconnexion() {
        assertThat(Journal.resumer(new Deconnexion("bob"))).isEqualTo("Deconnexion de bob");
    }

    @Test
    @DisplayName("resumer d'une erreur")
    void erreur() {
        assertThat(Journal.resumer(new Erreur(404, "Not Found")))
            .isEqualTo("Erreur 404 : Not Found");
    }
}
