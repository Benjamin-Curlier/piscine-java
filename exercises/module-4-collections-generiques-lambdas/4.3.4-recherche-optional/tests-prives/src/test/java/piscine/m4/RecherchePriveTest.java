package piscine.m4;

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

    private static final Membre MARTIN = new Membre("Martin", Niveau.CONFIRME,   3);
    private static final Membre LEBRUN = new Membre("Lebrun", Niveau.SENIOR,   7);

    @Test
    @DisplayName("premier : liste vide -> Optional.empty()")
    void premier_liste_vide() {
        Optional<Membre> opt = Recherche.premier(List.of(), s -> s.niveau() == Niveau.JUNIOR);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("plusHautNiveau : liste vide -> Optional.empty()")
    void plus_haut_niveau_liste_vide() {
        Optional<Membre> opt = Recherche.plusHautNiveau(List.of());
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("premier : aucun match -> Optional.empty()")
    void premier_aucun_match() {
        List<Membre> equipe = List.of(MARTIN, LEBRUN);
        Optional<Membre> opt = Recherche.premier(equipe, s -> s.niveau() == Niveau.PRINCIPAL);
        assertThat(opt).isEmpty();
    }

    @Test
    @DisplayName("nomOuParDefaut : liste vide -> Aucun")
    void nom_liste_vide() {
        String nom = Recherche.nomOuParDefaut(List.of(), s -> s.anciennete() > 0);
        assertThat(nom).isEqualTo("Aucun");
    }

    @Test
    @DisplayName("plusHautNiveau : liste a un seul element -> ce membre")
    void plus_haut_niveau_singleton() {
        Optional<Membre> opt = Recherche.plusHautNiveau(List.of(MARTIN));
        assertThat(opt).contains(MARTIN);
    }
}
