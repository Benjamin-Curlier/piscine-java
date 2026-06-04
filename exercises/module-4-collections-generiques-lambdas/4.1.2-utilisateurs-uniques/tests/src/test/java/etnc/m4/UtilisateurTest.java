package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.2 (equals/hashCode et unicite dans un Set).
 */
@DisplayName("Utilisateur — tests publics")
class UtilisateurTest {

    @Test
    @DisplayName("memes nom et matricule : utilisateurs egaux")
    void egalite_logique() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        Utilisateur b = new Utilisateur("Dupont", 1234);
        assertThat(a).isEqualTo(b);
    }

    @Test
    @DisplayName("champs differents : utilisateurs non egaux")
    void champs_differents() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        Utilisateur autreMatricule = new Utilisateur("Dupont", 9999);
        Utilisateur autreNom = new Utilisateur("Martin", 1234);
        assertThat(a).isNotEqualTo(autreMatricule);
        assertThat(a).isNotEqualTo(autreNom);
    }

    @Test
    @DisplayName("utilisateurs egaux : memes hashCode")
    void hashcode_coherent() {
        Utilisateur a = new Utilisateur("Dupont", 1234);
        Utilisateur b = new Utilisateur("Dupont", 1234);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("ajouter un nouvel utilisateur renvoie true, un doublon logique renvoie false")
    void ajouter_doublon() {
        RegistreUtilisateurs registre = new RegistreUtilisateurs();
        assertThat(registre.ajouter(new Utilisateur("Dupont", 1234))).isTrue();
        assertThat(registre.ajouter(new Utilisateur("Dupont", 1234))).isFalse();
    }

    @Test
    @DisplayName("nombreDistincts ignore les doublons logiques")
    void nombre_distincts_ignore_doublons() {
        RegistreUtilisateurs registre = new RegistreUtilisateurs();
        registre.ajouter(new Utilisateur("Dupont", 1234));
        registre.ajouter(new Utilisateur("Dupont", 1234));
        assertThat(registre.nombreDistincts()).isEqualTo(1);
    }
}
