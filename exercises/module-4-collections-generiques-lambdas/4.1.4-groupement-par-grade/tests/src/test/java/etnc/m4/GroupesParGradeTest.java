package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.4 (groupement de personnel par grade).
 */
@DisplayName("GroupesParGrade — tests publics")
class GroupesParGradeTest {

    @Test
    @DisplayName("affecter plusieurs noms au meme grade les accumule dans l'ordre")
    void affecter_accumule_dans_ordre() {
        GroupesParGrade g = new GroupesParGrade();
        g.affecter(Grade.SERGENT, "Dupont");
        g.affecter(Grade.SERGENT, "Martin");
        g.affecter(Grade.SERGENT, "Leclerc");
        assertThat(g.membres(Grade.SERGENT)).containsExactly("Dupont", "Martin", "Leclerc");
    }

    @Test
    @DisplayName("grades() renvoie exactement les grades ayant au moins un membre")
    void grades_ensemble_des_grades_affectes() {
        GroupesParGrade g = new GroupesParGrade();
        g.affecter(Grade.CAPORAL, "Bernard");
        g.affecter(Grade.LIEUTENANT, "Renard");
        g.affecter(Grade.CAPORAL, "Petit");
        assertThat(g.grades()).containsExactlyInAnyOrder(Grade.CAPORAL, Grade.LIEUTENANT);
    }

    @Test
    @DisplayName("effectif correspond au nombre de membres du grade")
    void effectif_par_grade() {
        GroupesParGrade g = new GroupesParGrade();
        g.affecter(Grade.ADJUDANT, "Moreau");
        g.affecter(Grade.ADJUDANT, "Simon");
        g.affecter(Grade.SOLDAT, "Thomas");
        assertThat(g.effectif(Grade.ADJUDANT)).isEqualTo(2);
        assertThat(g.effectif(Grade.SOLDAT)).isEqualTo(1);
    }

    @Test
    @DisplayName("un meme nom affecte deux fois au meme grade apparait deux fois")
    void meme_nom_deux_fois_apparait_deux_fois() {
        GroupesParGrade g = new GroupesParGrade();
        g.affecter(Grade.CAPORAL, "Durand");
        g.affecter(Grade.CAPORAL, "Durand");
        assertThat(g.membres(Grade.CAPORAL)).hasSize(2);
        assertThat(g.membres(Grade.CAPORAL)).containsExactly("Durand", "Durand");
    }
}
