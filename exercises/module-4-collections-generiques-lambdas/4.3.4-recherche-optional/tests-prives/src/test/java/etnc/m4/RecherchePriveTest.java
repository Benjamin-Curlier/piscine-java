package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 4.3.4 (cas limites Optional).
 */
@DisplayName("Recherche — tests prives")
class RecherchePriveTest {

    private static final Soldat MARTIN = new Soldat("Martin", Grade.CAPORAL,   3);
    private static final Soldat LEBRUN = new Soldat("Lebrun", Grade.SERGENT,   7);

    @Test
    @DisplayName("premier : liste vide -> Optional.empty()")
    void premier_liste_vide() {
        Optional<Soldat> opt = Recherche.premier(List.of(), s -> s.grade() == Grade.SOLDAT);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("plusHautGrade : liste vide -> Optional.empty()")
    void plus_haut_grade_liste_vide() {
        Optional<Soldat> opt = Recherche.plusHautGrade(List.of());
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("premier : aucun match -> Optional.empty()")
    void premier_aucun_match() {
        List<Soldat> troupe = List.of(MARTIN, LEBRUN);
        Optional<Soldat> opt = Recherche.premier(troupe, s -> s.grade() == Grade.LIEUTENANT);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("nomOuParDefaut : liste vide -> Aucun")
    void nom_liste_vide() {
        String nom = Recherche.nomOuParDefaut(List.of(), s -> s.anciennete() > 0);
        assertThat(nom).isEqualTo("Aucun");
    }

    @Test
    @DisplayName("plusHautGrade : liste a un seul element -> ce soldat")
    void plus_haut_grade_singleton() {
        Optional<Soldat> opt = Recherche.plusHautGrade(List.of(MARTIN));
        assertThat(opt).contains(MARTIN);
    }
}
