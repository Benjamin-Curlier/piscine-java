package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.1.4.
 */
@DisplayName("GroupesParNiveau — tests prives")
class GroupesParNiveauPriveTest {

    @Test
    @DisplayName("membres d'un niveau jamais affecte renvoie une liste vide (pas null)")
    void membres_niveau_absent_liste_vide() {
        GroupesParNiveau g = new GroupesParNiveau();
        assertThat(g.membres(Niveau.PRINCIPAL)).isEmpty();
    }

    @Test
    @DisplayName("affecter A puis B au meme niveau : les deux presents (computeIfAbsent ne reinitialise pas)")
    void affecter_ne_reinitialise_pas() {
        GroupesParNiveau g = new GroupesParNiveau();
        g.affecter(Niveau.SENIOR, "Alpha");
        g.affecter(Niveau.SENIOR, "Bravo");
        assertThat(g.membres(Niveau.SENIOR)).contains("Alpha", "Bravo");
    }

    @Test
    @DisplayName("effectif d'un niveau jamais affecte renvoie 0")
    void effectif_niveau_absent_zero() {
        GroupesParNiveau g = new GroupesParNiveau();
        assertThat(g.effectif(Niveau.LEAD)).isEqualTo(0);
    }
}
