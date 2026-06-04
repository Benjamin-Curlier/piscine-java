package etnc.m5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("5.3.3 — ParseurCommandes (tests privés)")
class ParseurCommandesPriveTest {

    @Test
    @DisplayName("ligne vide leve IllegalArgumentException avec message 'commande vide'")
    void testLigneVide() {
        assertThatThrownBy(() -> ParseurCommandes.executer(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("commande vide");
    }

    @Test
    @DisplayName("ligne blanche (espaces seuls) leve IllegalArgumentException")
    void testLigneBlanche() {
        assertThatThrownBy(() -> ParseurCommandes.executer("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("commande vide");
    }

    @Test
    @DisplayName("espaces multiples entre tokens toleres (somme   3    4 -> 7)")
    void testEspacesMultiples() {
        assertThat(ParseurCommandes.executer("somme   3    4")).isEqualTo(7);
    }

    @Test
    @DisplayName("espaces en tete et queue ignores (  oppose 5  -> -5)")
    void testEspacesTesteQueue() {
        assertThat(ParseurCommandes.executer("  oppose 5 ")).isEqualTo(-5);
    }

    @Test
    @DisplayName("arguments negatifs acceptes (somme -3 4 -> 1)")
    void testArgumentsNegatifs() {
        assertThat(ParseurCommandes.executer("somme -3 4")).isEqualTo(1);
    }

    @Test
    @DisplayName("message commande inconnue contient le nom de la commande")
    void testMessageCommandeInconnue() {
        assertThatThrownBy(() -> ParseurCommandes.executer("racine 9"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("racine");
    }

    @Test
    @DisplayName("diff non commutatif : diff 3 10 renvoie -7")
    void testDiffNonCommutatif() {
        assertThat(ParseurCommandes.executer("diff 3 10")).isEqualTo(-7);
    }

    @Test
    @DisplayName("oppose 0 renvoie 0")
    void testOppose0() {
        assertThat(ParseurCommandes.executer("oppose 0")).isEqualTo(0);
    }

    @Test
    @DisplayName("casse sensible : SOMME 1 2 est une commande inconnue")
    void testCasseSensible() {
        assertThatThrownBy(() -> ParseurCommandes.executer("SOMME 1 2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SOMME");
    }
}
