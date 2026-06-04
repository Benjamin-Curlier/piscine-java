package etnc.m4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 4.3.4 (Recherche avec Optional).
 */
@DisplayName("Recherche — tests publics")
class RechercheTest {

    private static final Soldat MARTIN  = new Soldat("Martin",  Grade.CAPORAL,    3);
    private static final Soldat LEBRUN  = new Soldat("Lebrun",  Grade.SERGENT,    7);
    private static final Soldat DUPONT  = new Soldat("Dupont",  Grade.LIEUTENANT, 12);

    private final List<Soldat> troupe = List.of(MARTIN, LEBRUN, DUPONT);

    @Test
    @DisplayName("premier : present quand un element satisfait")
    void premier_present() {
        Optional<Soldat> opt = Recherche.premier(troupe, s -> s.anciennete() > 5);
        assertThat(opt).isPresent();
        assertThat(opt).contains(LEBRUN);
    }

    @Test
    @DisplayName("premier : vide quand aucun element ne satisfait")
    void premier_vide() {
        Optional<Soldat> opt = Recherche.premier(troupe, s -> s.anciennete() > 100);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("plusHautGrade : contient le soldat de grade maximum")
    void plus_haut_grade() {
        Optional<Soldat> opt = Recherche.plusHautGrade(troupe);
        assertThat(opt).contains(DUPONT);
    }

    @Test
    @DisplayName("nomOuParDefaut : renvoie le nom quand trouve")
    void nom_ou_par_defaut_trouve() {
        String nom = Recherche.nomOuParDefaut(troupe, s -> s.grade() == Grade.SERGENT);
        assertThat(nom).isEqualTo("Lebrun");
    }

    @Test
    @DisplayName("nomOuParDefaut : renvoie Aucun quand pas de match")
    void nom_ou_par_defaut_absent() {
        String nom = Recherche.nomOuParDefaut(troupe, s -> s.grade() == Grade.ADJUDANT);
        assertThat(nom).isEqualTo("Aucun");
    }
}
