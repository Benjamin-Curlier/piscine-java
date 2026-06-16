package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests publics de l'exercice 3.4.2 Niveau.
 */
@DisplayName("Niveau — tests publics")
class NiveauTest {

    @Test
    @DisplayName("solde de base d'une constante")
    void solde_base() {
        assertThat(Niveau.JUNIOR.getSoldeBase()).isEqualTo(1600.0, within(1e-9));
    }

    @Test
    @DisplayName("categorie selon la constante")
    void categorie() {
        assertThat(Niveau.JUNIOR.categorie()).isEqualTo("Débutant");
        assertThat(Niveau.SENIOR.categorie()).isEqualTo("Intermédiaire");
        assertThat(Niveau.PRINCIPAL.categorie()).isEqualTo("Expert");
    }

    @Test
    @DisplayName("nombre de constantes")
    void valeurs() {
        assertThat(Niveau.values()).hasSize(5);
    }
}
