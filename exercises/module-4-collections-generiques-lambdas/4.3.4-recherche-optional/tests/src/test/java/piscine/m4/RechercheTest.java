package piscine.m4;

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

    private static final Membre MARTIN  = new Membre("Martin",  Niveau.CONFIRME,    3);
    private static final Membre LEBRUN  = new Membre("Lebrun",  Niveau.SENIOR,    7);
    private static final Membre DUPONT  = new Membre("Dupont",  Niveau.PRINCIPAL, 12);

    private final List<Membre> equipe = List.of(MARTIN, LEBRUN, DUPONT);

    @Test
    @DisplayName("premier : present quand un element satisfait")
    void premier_present() {
        Optional<Membre> opt = Recherche.premier(equipe, s -> s.anciennete() > 5);
        assertThat(opt).isPresent();
        assertThat(opt).contains(LEBRUN);
    }

    @Test
    @DisplayName("premier : vide quand aucun element ne satisfait")
    void premier_vide() {
        Optional<Membre> opt = Recherche.premier(equipe, s -> s.anciennete() > 100);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("plusHautNiveau : contient le membre de niveau maximum")
    void plus_haut_niveau() {
        Optional<Membre> opt = Recherche.plusHautNiveau(equipe);
        assertThat(opt).contains(DUPONT);
    }

    @Test
    @DisplayName("nomOuParDefaut : renvoie le nom quand trouve")
    void nom_ou_par_defaut_trouve() {
        String nom = Recherche.nomOuParDefaut(equipe, s -> s.niveau() == Niveau.SENIOR);
        assertThat(nom).isEqualTo("Lebrun");
    }

    @Test
    @DisplayName("nomOuParDefaut : renvoie Aucun quand pas de match")
    void nom_ou_par_defaut_absent() {
        String nom = Recherche.nomOuParDefaut(equipe, s -> s.niveau() == Niveau.LEAD);
        assertThat(nom).isEqualTo("Aucun");
    }
}
