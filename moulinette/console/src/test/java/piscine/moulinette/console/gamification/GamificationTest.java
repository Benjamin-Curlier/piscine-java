package piscine.moulinette.console.gamification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Gamification — XP, niveaux, badges")
class GamificationTest {

    // total XP possible = 10 + 15 + 15 + 25 + 40 = 105
    private static final List<Exo> EXOS = List.of(
        new Exo("1.1.1", 1, Difficulte.TRES_FACILE),
        new Exo("1.1.2", 1, Difficulte.FACILE),
        new Exo("1.1.3", 1, Difficulte.FACILE),
        new Exo("2.1.1", 2, Difficulte.MOYEN),
        new Exo("2.1.2", 2, Difficulte.DIFFICILE));

    @Test
    @DisplayName("aucun exo validé : niveau 1 Débutant, 0 XP, 0 %, aucun badge")
    void vide() {
        Profil p = Gamification.evaluer(Set.of(), EXOS);
        assertThat(p.xp()).isZero();
        assertThat(p.niveau()).isEqualTo(1);
        assertThat(p.titre()).isEqualTo("Débutant");
        assertThat(p.progressionPourcent()).isZero();
        assertThat(p.badges()).isEmpty();
    }

    @Test
    @DisplayName("XP = somme des difficultés validées ; badge « Premier sang »")
    void xp_et_premier_sang() {
        Profil p = Gamification.evaluer(Set.of("1.1.1", "1.1.2"), EXOS); // 10 + 15
        assertThat(p.xp()).isEqualTo(25);
        assertThat(p.progressionPourcent()).isEqualTo(40); // 2 / 5
        assertThat(p.badges()).extracting(Badge::id).containsExactly("premier-sang");
    }

    @Test
    @DisplayName("module entièrement validé → badge « Maître du module »")
    void maitre_de_module() {
        Profil p = Gamification.evaluer(Set.of("1.1.1", "1.1.2", "1.1.3"), EXOS);
        assertThat(p.badges()).extracting(Badge::id)
            .contains("module-1")
            .doesNotContain("module-2");
    }

    @Test
    @DisplayName("tout validé → 100 %, badges Diplômé + tous les modules, pas « Mi-parcours »")
    void tout_valide() {
        Profil p = Gamification.evaluer(
            Set.of("1.1.1", "1.1.2", "1.1.3", "2.1.1", "2.1.2"), EXOS);
        assertThat(p.xp()).isEqualTo(105);
        assertThat(p.progressionPourcent()).isEqualTo(100);
        assertThat(p.badges()).extracting(Badge::id)
            .contains("diplome", "module-1", "module-2")
            .doesNotContain("mi-parcours");
    }

    @Test
    @DisplayName("le niveau monte avec l'XP (≥100 XP → Apprenti)")
    void niveau_monte() {
        assertThat(Gamification.evaluer(Set.of(), EXOS).niveau()).isEqualTo(1);
        Profil p = Gamification.evaluer(
            Set.of("1.1.1", "1.1.2", "1.1.3", "2.1.1", "2.1.2"), EXOS); // 105 XP
        assertThat(p.niveau()).isEqualTo(2);
        assertThat(p.titre()).isEqualTo("Apprenti");
        assertThat(p.xpProchainNiveau()).isEqualTo(250);
    }

    @Test
    @DisplayName("la conversion de difficulté depuis le metadata est tolérante")
    void difficulte_depuis_metadata() {
        assertThat(Difficulte.depuis("tres-facile")).isEqualTo(Difficulte.TRES_FACILE);
        assertThat(Difficulte.depuis("MOYEN")).isEqualTo(Difficulte.MOYEN);
        assertThat(Difficulte.depuis(null)).isEqualTo(Difficulte.FACILE);
        assertThat(Difficulte.depuis("inconnu")).isEqualTo(Difficulte.FACILE);
    }
}
