package piscine.m4;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.2.3 (cas limites Comparable<Membre>).
 */
@DisplayName("Membre Comparable — tests prives")
class MembrePriveTest {

    @Test
    @DisplayName("meme niveau ET meme nom : compareTo zero")
    void egalite_stricte() {
        Membre a = new Membre("Dupont", Niveau.CONFIRME, 1);
        Membre b = new Membre("Dupont", Niveau.CONFIRME, 7);
        assertThat(a.compareTo(b)).isZero();
    }

    @Test
    @DisplayName("tri stable sur niveau egal : ordre par nom")
    void tri_niveau_egal_ordre_nom() {
        Membre charles = new Membre("Charles", Niveau.SENIOR, 3);
        Membre alice   = new Membre("Alice",   Niveau.SENIOR, 1);
        Membre bernard = new Membre("Bernard", Niveau.SENIOR, 5);

        List<Membre> liste = new ArrayList<>(List.of(charles, alice, bernard));
        liste.sort(null);

        assertThat(liste).containsExactly(alice, bernard, charles);
    }

    @Test
    @DisplayName("antisymetrie : a.compareTo(b) et b.compareTo(a) ont des signes opposes")
    void antisymetrie() {
        Membre a = new Membre("Arnaud", Niveau.LEAD,  2);
        Membre b = new Membre("Martin", Niveau.CONFIRME,   4);
        int ab = a.compareTo(b);
        int ba = b.compareTo(a);
        assertThat(Integer.signum(ab)).isEqualTo(-Integer.signum(ba));
    }
}
