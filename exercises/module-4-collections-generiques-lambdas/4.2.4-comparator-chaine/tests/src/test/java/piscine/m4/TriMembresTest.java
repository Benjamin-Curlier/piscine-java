package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.2.4 (chaînage de Comparator).
 */
@DisplayName("TriMembres — tests publics")
class TriMembresTest {

    private static final Membre ALPHA   = new Membre("Alpha",   Niveau.SENIOR,    3);
    private static final Membre BRAVO   = new Membre("Bravo",   Niveau.PRINCIPAL, 7);
    private static final Membre CHARLIE = new Membre("Charlie", Niveau.CONFIRME,    1);
    private static final Membre DELTA   = new Membre("Delta",   Niveau.JUNIOR,     2);

    @Test
    @DisplayName("parNom trie par ordre alphabétique")
    void par_nom() {
        List<Membre> membres = List.of(DELTA, BRAVO, ALPHA, CHARLIE);
        List<Membre> resultat = TriMembres.parNom(membres);
        assertThat(resultat).containsExactly(ALPHA, BRAVO, CHARLIE, DELTA);
    }

    @Test
    @DisplayName("parNiveauPuisNom trie par niveau croissant puis par nom")
    void par_niveau_puis_nom() {
        List<Membre> membres = List.of(ALPHA, BRAVO, CHARLIE, DELTA);
        List<Membre> resultat = TriMembres.parNiveauPuisNom(membres);
        // JUNIOR < CONFIRME < SENIOR < PRINCIPAL
        assertThat(resultat).containsExactly(DELTA, CHARLIE, ALPHA, BRAVO);
    }

    @Test
    @DisplayName("parNiveauDecroissant trie par niveau décroissant")
    void par_niveau_decroissant() {
        List<Membre> membres = List.of(DELTA, CHARLIE, ALPHA, BRAVO);
        List<Membre> resultat = TriMembres.parNiveauDecroissant(membres);
        // PRINCIPAL > SENIOR > CONFIRME > JUNIOR
        assertThat(resultat).containsExactly(BRAVO, ALPHA, CHARLIE, DELTA);
    }
}
