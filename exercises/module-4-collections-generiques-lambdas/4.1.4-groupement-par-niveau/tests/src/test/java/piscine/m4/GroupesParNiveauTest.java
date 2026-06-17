package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.1.4 (groupement de membres par niveau).
 */
@DisplayName("GroupesParNiveau — tests publics")
class GroupesParNiveauTest {

    @Test
    @DisplayName("affecter plusieurs noms au meme niveau les accumule dans l'ordre")
    void affecter_accumule_dans_ordre() {
        GroupesParNiveau g = new GroupesParNiveau();
        g.affecter(Niveau.SENIOR, "Dupont");
        g.affecter(Niveau.SENIOR, "Martin");
        g.affecter(Niveau.SENIOR, "Leclerc");
        assertThat(g.membres(Niveau.SENIOR)).containsExactly("Dupont", "Martin", "Leclerc");
    }

    @Test
    @DisplayName("niveaux() renvoie exactement les niveaux ayant au moins un membre")
    void niveaux_ensemble_des_niveaux_affectes() {
        GroupesParNiveau g = new GroupesParNiveau();
        g.affecter(Niveau.CONFIRME, "Bernard");
        g.affecter(Niveau.PRINCIPAL, "Renard");
        g.affecter(Niveau.CONFIRME, "Petit");
        assertThat(g.niveaux()).containsExactlyInAnyOrder(Niveau.CONFIRME, Niveau.PRINCIPAL);
    }

    @Test
    @DisplayName("effectif correspond au nombre de membres du niveau")
    void effectif_par_niveau() {
        GroupesParNiveau g = new GroupesParNiveau();
        g.affecter(Niveau.LEAD, "Moreau");
        g.affecter(Niveau.LEAD, "Simon");
        g.affecter(Niveau.JUNIOR, "Thomas");
        assertThat(g.effectif(Niveau.LEAD)).isEqualTo(2);
        assertThat(g.effectif(Niveau.JUNIOR)).isEqualTo(1);
    }

    @Test
    @DisplayName("un meme nom affecte deux fois au meme niveau apparait deux fois")
    void meme_nom_deux_fois_apparait_deux_fois() {
        GroupesParNiveau g = new GroupesParNiveau();
        g.affecter(Niveau.CONFIRME, "Durand");
        g.affecter(Niveau.CONFIRME, "Durand");
        assertThat(g.membres(Niveau.CONFIRME)).hasSize(2);
        assertThat(g.membres(Niveau.CONFIRME)).containsExactly("Durand", "Durand");
    }
}
