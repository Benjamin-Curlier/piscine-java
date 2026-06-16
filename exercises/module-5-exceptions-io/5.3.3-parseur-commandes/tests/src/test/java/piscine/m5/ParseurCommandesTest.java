package piscine.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("5.3.3 — ParseurCommandes (tests publics)")
class ParseurCommandesTest {

    @Test
    @DisplayName("somme 3 4 renvoie 7")
    void testSomme() {
        assertThat(ParseurCommandes.executer("somme 3 4")).isEqualTo(7);
    }

    @Test
    @DisplayName("diff 10 3 renvoie 7")
    void testDiff() {
        assertThat(ParseurCommandes.executer("diff 10 3")).isEqualTo(7);
    }

    @Test
    @DisplayName("produit 2 5 renvoie 10")
    void testProduit() {
        assertThat(ParseurCommandes.executer("produit 2 5")).isEqualTo(10);
    }

    @Test
    @DisplayName("oppose 8 renvoie -8")
    void testOppose() {
        assertThat(ParseurCommandes.executer("oppose 8")).isEqualTo(-8);
    }

    @Test
    @DisplayName("commande inconnue leve IllegalArgumentException")
    void testCommandeInconnue() {
        assertThatThrownBy(() -> ParseurCommandes.executer("racine 9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("argument manquant (somme 3) leve IllegalArgumentException")
    void testArgumentManquant() {
        assertThatThrownBy(() -> ParseurCommandes.executer("somme 3"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("argument en trop (oppose 1 2) leve IllegalArgumentException")
    void testArgumentEnTrop() {
        assertThatThrownBy(() -> ParseurCommandes.executer("oppose 1 2"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("argument non numerique (somme a b) leve NumberFormatException")
    void testArgumentNonNumerique() {
        assertThatThrownBy(() -> ParseurCommandes.executer("somme a b"))
                .isInstanceOf(NumberFormatException.class);
    }
}
