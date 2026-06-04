package etnc.m4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.3 (Comparable sur Soldat).
 */
@DisplayName("Soldat Comparable — tests publics")
class SoldatTest {

    @Test
    @DisplayName("tri par grade croissant sur liste melangee")
    void tri_par_grade() {
        Soldat lieutenant = new Soldat("Martin", Grade.LIEUTENANT, 2);
        Soldat soldat     = new Soldat("Dupont", Grade.SOLDAT,     5);
        Soldat sergent    = new Soldat("Girard", Grade.SERGENT,    1);
        Soldat caporal    = new Soldat("Arnaud", Grade.CAPORAL,    3);

        List<Soldat> liste = new ArrayList<>(List.of(lieutenant, soldat, sergent, caporal));
        Collections.sort(liste);

        assertThat(liste).containsExactly(soldat, caporal, sergent, lieutenant);
    }

    @Test
    @DisplayName("compareTo grade inferieur vs superieur : negatif")
    void compare_grade_inferieur_vs_superieur() {
        Soldat bas  = new Soldat("X", Grade.SOLDAT,     1);
        Soldat haut = new Soldat("Y", Grade.LIEUTENANT, 1);
        assertThat(bas.compareTo(haut)).isNegative();
    }

    @Test
    @DisplayName("compareTo grade superieur vs inferieur : positif")
    void compare_grade_superieur_vs_inferieur() {
        Soldat haut = new Soldat("X", Grade.ADJUDANT, 1);
        Soldat bas  = new Soldat("Y", Grade.CAPORAL,  1);
        assertThat(haut.compareTo(bas)).isPositive();
    }

    @Test
    @DisplayName("a grade egal, depart par nom alphabetique")
    void departage_par_nom_a_grade_egal() {
        Soldat alice = new Soldat("Alice", Grade.SERGENT, 1);
        Soldat zorro = new Soldat("Zorro", Grade.SERGENT, 1);
        assertThat(alice.compareTo(zorro)).isNegative();
        assertThat(zorro.compareTo(alice)).isPositive();
    }
}
