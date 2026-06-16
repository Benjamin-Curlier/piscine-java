package piscine.m4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.3 (Comparable sur Membre).
 */
@DisplayName("Membre Comparable — tests publics")
class MembreTest {

    @Test
    @DisplayName("tri par niveau croissant sur liste melangee")
    void tri_par_niveau() {
        Membre lieutenant = new Membre("Martin", Niveau.PRINCIPAL, 2);
        Membre membre     = new Membre("Dupont", Niveau.JUNIOR,     5);
        Membre sergent    = new Membre("Girard", Niveau.SENIOR,    1);
        Membre caporal    = new Membre("Arnaud", Niveau.CONFIRME,    3);

        List<Membre> liste = new ArrayList<>(List.of(lieutenant, membre, sergent, caporal));
        Collections.sort(liste);

        assertThat(liste).containsExactly(membre, caporal, sergent, lieutenant);
    }

    @Test
    @DisplayName("compareTo niveau inferieur vs superieur : negatif")
    void compare_niveau_inferieur_vs_superieur() {
        Membre bas  = new Membre("X", Niveau.JUNIOR,     1);
        Membre haut = new Membre("Y", Niveau.PRINCIPAL, 1);
        assertThat(bas.compareTo(haut)).isNegative();
    }

    @Test
    @DisplayName("compareTo niveau superieur vs inferieur : positif")
    void compare_niveau_superieur_vs_inferieur() {
        Membre haut = new Membre("X", Niveau.LEAD, 1);
        Membre bas  = new Membre("Y", Niveau.CONFIRME,  1);
        assertThat(haut.compareTo(bas)).isPositive();
    }

    @Test
    @DisplayName("a niveau egal, depart par nom alphabetique")
    void departage_par_nom_a_niveau_egal() {
        Membre alice = new Membre("Alice", Niveau.SENIOR, 1);
        Membre zorro = new Membre("Zorro", Niveau.SENIOR, 1);
        assertThat(alice.compareTo(zorro)).isNegative();
        assertThat(zorro.compareTo(alice)).isPositive();
    }
}
