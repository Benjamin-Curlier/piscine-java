package piscine.m4;

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
    @DisplayName("parNiveau sur liste vide renvoie une map vide")
    void par_niveau_liste_vide() {
        Map<Niveau, List<Membre>> carte = Regroupement.parNiveau(List.of());
        assertThat(carte).isEmpty();
    }

    @Test
    @DisplayName("selonAnciennete garantit toujours les deux cles true ET false meme si un groupe est vide")
    void selon_anciennete_deux_cles_toujours_presentes() {
        // Tous les membres ont anciennete >= 0, seuil = 0 => tous dans true, false vide
        List<Membre> tous = List.of(
                new Membre("Alpha", Niveau.PRINCIPAL, 20),
                new Membre("Beta",  Niveau.LEAD,   10)
        );
        Map<Boolean, List<Membre>> carte = Regroupement.selonAnciennete(tous, 0);
        assertThat(carte).containsKeys(true, false);
        assertThat(carte.get(false)).isEmpty();
        assertThat(carte.get(true)).hasSize(2);
    }

    @Test
    @DisplayName("parNiveau avec un seul niveau produit une seule cle")
    void par_niveau_un_seul_niveau() {
        List<Membre> mononiveau = List.of(
                new Membre("X", Niveau.CONFIRME, 2),
                new Membre("Y", Niveau.CONFIRME, 4)
        );
        Map<Niveau, List<Membre>> carte = Regroupement.parNiveau(mononiveau);
        assertThat(carte).hasSize(1);
        assertThat(carte).containsKey(Niveau.CONFIRME);
        assertThat(carte.get(Niveau.CONFIRME)).hasSize(2);
    }
}
