package piscine.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.3.1 (filtrage et projection par stream).
 */
@DisplayName("Effectifs — tests publics")
class EffectifsTest {

    private static final List<Membre> EQUIPE = List.of(
            new Membre("Dupont",  Niveau.JUNIOR,     2),
            new Membre("Martin",  Niveau.SENIOR,     8),
            new Membre("Bernard", Niveau.CONFIRME,     4),
            new Membre("Leroy",   Niveau.PRINCIPAL, 15),
            new Membre("Moreau",  Niveau.LEAD,    6)
    );

    @Test
    @DisplayName("nomsDesNiveauxAuMoins retourne les noms des niveaux >= SENIOR dans l'ordre source")
    void noms_des_niveaux_au_moins_senior() {
        List<String> noms = Effectifs.nomsDesNiveauxAuMoins(EQUIPE, Niveau.SENIOR);
        assertThat(noms).containsExactly("Martin", "Leroy", "Moreau");
    }

    @Test
    @DisplayName("filtrer par anciennete > 5 renvoie le bon sous-ensemble")
    void filtrer_par_anciennete() {
        List<Membre> resultat = Effectifs.filtrer(EQUIPE, s -> s.anciennete() > 5);
        assertThat(resultat).containsExactly(
                new Membre("Martin",  Niveau.SENIOR,     8),
                new Membre("Leroy",   Niveau.PRINCIPAL, 15),
                new Membre("Moreau",  Niveau.LEAD,    6)
        );
    }

    @Test
    @DisplayName("filtrer par niveau PRINCIPAL renvoie uniquement les principaux")
    void filtrer_par_niveau_principal() {
        List<Membre> resultat = Effectifs.filtrer(EQUIPE, s -> s.niveau() == Niveau.PRINCIPAL);
        assertThat(resultat).containsExactly(
                new Membre("Leroy", Niveau.PRINCIPAL, 15)
        );
    }
}
