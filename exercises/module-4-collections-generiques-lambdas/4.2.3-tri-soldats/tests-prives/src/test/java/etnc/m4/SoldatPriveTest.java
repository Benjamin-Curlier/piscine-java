package etnc.m4;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.2.3 (cas limites Comparable<Soldat>).
 */
@DisplayName("Soldat Comparable — tests prives")
class SoldatPriveTest {

    @Test
    @DisplayName("meme grade ET meme nom : compareTo zero")
    void egalite_stricte() {
        Soldat a = new Soldat("Dupont", Grade.CAPORAL, 1);
        Soldat b = new Soldat("Dupont", Grade.CAPORAL, 7);
        assertThat(a.compareTo(b)).isZero();
    }

    @Test
    @DisplayName("tri stable sur grade egal : ordre par nom")
    void tri_grade_egal_ordre_nom() {
        Soldat charles = new Soldat("Charles", Grade.SERGENT, 3);
        Soldat alice   = new Soldat("Alice",   Grade.SERGENT, 1);
        Soldat bernard = new Soldat("Bernard", Grade.SERGENT, 5);

        List<Soldat> liste = new ArrayList<>(List.of(charles, alice, bernard));
        liste.sort(null);

        assertThat(liste).containsExactly(alice, bernard, charles);
    }

    @Test
    @DisplayName("antisymetrie : a.compareTo(b) et b.compareTo(a) ont des signes opposes")
    void antisymetrie() {
        Soldat a = new Soldat("Arnaud", Grade.ADJUDANT,  2);
        Soldat b = new Soldat("Martin", Grade.CAPORAL,   4);
        int ab = a.compareTo(b);
        int ba = b.compareTo(a);
        assertThat(Integer.signum(ab)).isEqualTo(-Integer.signum(ba));
    }
}
