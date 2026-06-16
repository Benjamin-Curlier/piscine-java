package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.4.4 Evenement.
 */
@DisplayName("Evenement — tests privés")
class EvenementPriveTest {

    @Test
    @DisplayName("resumer sur un tableau hétérogène d'événements")
    void tableau() {
        Evenement[] evenements = {
            new Connexion("alice"),
            new Erreur(500, "Boom"),
            new Deconnexion("alice")
        };
        assertThat(Journal.resumer(evenements[0])).isEqualTo("Connexion de alice");
        assertThat(Journal.resumer(evenements[1])).isEqualTo("Erreur 500 : Boom");
        assertThat(Journal.resumer(evenements[2])).isEqualTo("Deconnexion de alice");
    }

    @Test
    @DisplayName("accesseur de record")
    void accesseur() {
        assertThat(new Connexion("bob").utilisateur()).isEqualTo("bob");
    }

    @Test
    @DisplayName("equals généré d'un record")
    void egalite() {
        assertThat(new Erreur(1, "x")).isEqualTo(new Erreur(1, "x"));
    }
}
