package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.3.1 (filtrage et projection par stream).
 */
@DisplayName("Effectifs — tests publics")
class EffectifsTest {

    private static final List<Soldat> TROUPE = List.of(
            new Soldat("Dupont",  Grade.SOLDAT,     2),
            new Soldat("Martin",  Grade.SERGENT,     8),
            new Soldat("Bernard", Grade.CAPORAL,     4),
            new Soldat("Leroy",   Grade.LIEUTENANT, 15),
            new Soldat("Moreau",  Grade.ADJUDANT,    6)
    );

    @Test
    @DisplayName("nomsDesGradesAuMoins retourne les noms des grades >= SERGENT dans l'ordre source")
    void noms_des_grades_au_moins_sergent() {
        List<String> noms = Effectifs.nomsDesGradesAuMoins(TROUPE, Grade.SERGENT);
        assertThat(noms).containsExactly("Martin", "Leroy", "Moreau");
    }

    @Test
    @DisplayName("filtrer par anciennete > 5 renvoie le bon sous-ensemble")
    void filtrer_par_anciennete() {
        List<Soldat> resultat = Effectifs.filtrer(TROUPE, s -> s.anciennete() > 5);
        assertThat(resultat).containsExactly(
                new Soldat("Martin",  Grade.SERGENT,     8),
                new Soldat("Leroy",   Grade.LIEUTENANT, 15),
                new Soldat("Moreau",  Grade.ADJUDANT,    6)
        );
    }

    @Test
    @DisplayName("filtrer par grade LIEUTENANT renvoie uniquement les lieutenants")
    void filtrer_par_grade_lieutenant() {
        List<Soldat> resultat = Effectifs.filtrer(TROUPE, s -> s.grade() == Grade.LIEUTENANT);
        assertThat(resultat).containsExactly(
                new Soldat("Leroy", Grade.LIEUTENANT, 15)
        );
    }
}
