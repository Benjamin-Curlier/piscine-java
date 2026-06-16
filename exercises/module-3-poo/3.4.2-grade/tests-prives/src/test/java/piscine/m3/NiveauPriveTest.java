package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests privés de l'exercice 3.4.2 Niveau.
 */
@DisplayName("Niveau — tests privés")
class NiveauPriveTest {

    @Test
    @DisplayName("solde de base du niveau le plus élevé")
    void solde_principal() {
        assertThat(Niveau.PRINCIPAL.getSoldeBase()).isEqualTo(3000.0, within(1e-9));
    }

    @Test
    @DisplayName("categorie des niveaux intermédiaires")
    void categorie_intermediaire() {
        assertThat(Niveau.CONFIRME.categorie()).isEqualTo("Débutant");
        assertThat(Niveau.LEAD.categorie()).isEqualTo("Intermédiaire");
    }

    @Test
    @DisplayName("valueOf retrouve la constante")
    void value_of() {
        assertThat(Niveau.valueOf("SENIOR")).isEqualTo(Niveau.SENIOR);
    }
}
