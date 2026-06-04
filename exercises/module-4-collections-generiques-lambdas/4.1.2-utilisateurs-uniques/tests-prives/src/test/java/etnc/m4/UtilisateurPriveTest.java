package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.1.2 (contrat equals gradue + robustesse du HashSet).
 */
@DisplayName("Utilisateur — tests prives")
class UtilisateurPriveTest {

    @Test
    @DisplayName("reflexivite : a.equals(a)")
    void reflexivite() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        assertThat(a.equals(a)).isTrue();
    }

    @Test
    @DisplayName("symetrie : a.equals(b) et b.equals(a)")
    void symetrie() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        Utilisateur b = new Utilisateur("Dupont", 1234);
        assertThat(a.equals(b)).isTrue();
        assertThat(b.equals(a)).isTrue();
    }

    @Test
    @DisplayName("equals(null) renvoie false (sans exception)")
    void equals_null() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        assertThat(a.equals(null)).isFalse();
    }

    @Test
    @DisplayName("equals d'un autre type renvoie false (sans exception)")
    void equals_autre_type() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        assertThat(a.equals("texte")).isFalse();
    }

    @Test
    @DisplayName("robustesse HashSet : 2 egaux + 1 different -> 2 distincts, copie egale reconnue")
    void robustesse_hashset() {
        RegistreUtilisateurs registre = new RegistreUtilisateurs();
        registre.ajouter(new Utilisateur("Dupont", 1234));
        registre.ajouter(new Utilisateur("Dupont", 1234));
        registre.ajouter(new Utilisateur("Martin", 5678));
        assertThat(registre.nombreDistincts()).isEqualTo(2);
        assertThat(registre.contient(new Utilisateur("Dupont", 1234))).isTrue();
    }
}
