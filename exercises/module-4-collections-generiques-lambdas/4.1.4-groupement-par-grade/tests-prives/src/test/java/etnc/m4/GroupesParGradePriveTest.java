package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.1.4.
 */
@DisplayName("GroupesParGrade — tests prives")
class GroupesParGradePriveTest {

    @Test
    @DisplayName("membres d'un grade jamais affecte renvoie une liste vide (pas null)")
    void membres_grade_absent_liste_vide() {
        GroupesParGrade g = new GroupesParGrade();
        assertThat(g.membres(Grade.LIEUTENANT)).isEmpty();
    }

    @Test
    @DisplayName("affecter A puis B au meme grade : les deux presents (computeIfAbsent ne reinitialise pas)")
    void affecter_ne_reinitialise_pas() {
        GroupesParGrade g = new GroupesParGrade();
        g.affecter(Grade.SERGENT, "Alpha");
        g.affecter(Grade.SERGENT, "Bravo");
        assertThat(g.membres(Grade.SERGENT)).contains("Alpha", "Bravo");
    }

    @Test
    @DisplayName("effectif d'un grade jamais affecte renvoie 0")
    void effectif_grade_absent_zero() {
        GroupesParGrade g = new GroupesParGrade();
        assertThat(g.effectif(Grade.ADJUDANT)).isEqualTo(0);
    }
}
