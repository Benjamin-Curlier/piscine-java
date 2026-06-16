package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 4.2.4.
 */
@DisplayName("TriMembres — tests privés")
class TriMembresPriveTest {

    @Test
    @DisplayName("parNiveauPuisNom départage deux membres de même niveau par le nom")
    void chaine_meme_niveau_departage_par_nom() {
        Membre abel  = new Membre("Abel",  Niveau.SENIOR, 5);
        Membre zorak = new Membre("Zorak", Niveau.SENIOR, 3);
        List<Membre> membres = List.of(zorak, abel);
        List<Membre> resultat = TriMembres.parNiveauPuisNom(membres);
        assertThat(resultat).containsExactly(abel, zorak);
    }

    @Test
    @DisplayName("la liste source n'est pas mutée après parNom")
    void source_non_mutee() {
        Membre delta = new Membre("Delta", Niveau.JUNIOR,     2);
        Membre alpha = new Membre("Alpha", Niveau.PRINCIPAL, 9);
        List<Membre> source = new ArrayList<>(List.of(delta, alpha));
        TriMembres.parNom(source);
        assertThat(source).containsExactly(delta, alpha);
    }

    @Test
    @DisplayName("liste à un élément renvoyée inchangée")
    void liste_un_element() {
        Membre seul = new Membre("Seul", Niveau.CONFIRME, 1);
        List<Membre> source = List.of(seul);
        assertThat(TriMembres.parNom(source)).containsExactly(seul);
        assertThat(TriMembres.parNiveauPuisNom(source)).containsExactly(seul);
        assertThat(TriMembres.parNiveauDecroissant(source)).containsExactly(seul);
    }
}
