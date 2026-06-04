package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 4.3.3.
 */
@DisplayName("Regroupement — tests privés")
class RegroupementPriveTest {

    @Test
    @DisplayName("parGrade sur liste vide renvoie une map vide")
    void par_grade_liste_vide() {
        Map<Grade, List<Soldat>> carte = Regroupement.parGrade(List.of());
        assertThat(carte).isEmpty();
    }

    @Test
    @DisplayName("selonAnciennete garantit toujours les deux cles true ET false meme si un groupe est vide")
    void selon_anciennete_deux_cles_toujours_presentes() {
        // Tous les soldats ont anciennete >= 0, seuil = 0 => tous dans true, false vide
        List<Soldat> tous = List.of(
                new Soldat("Alpha", Grade.LIEUTENANT, 20),
                new Soldat("Beta",  Grade.ADJUDANT,   10)
        );
        Map<Boolean, List<Soldat>> carte = Regroupement.selonAnciennete(tous, 0);
        assertThat(carte).containsKeys(true, false);
        assertThat(carte.get(false)).isEmpty();
        assertThat(carte.get(true)).hasSize(2);
    }

    @Test
    @DisplayName("parGrade avec un seul grade produit une seule cle")
    void par_grade_un_seul_grade() {
        List<Soldat> monograde = List.of(
                new Soldat("X", Grade.CAPORAL, 2),
                new Soldat("Y", Grade.CAPORAL, 4)
        );
        Map<Grade, List<Soldat>> carte = Regroupement.parGrade(monograde);
        assertThat(carte).hasSize(1);
        assertThat(carte).containsKey(Grade.CAPORAL);
        assertThat(carte.get(Grade.CAPORAL)).hasSize(2);
    }
}
