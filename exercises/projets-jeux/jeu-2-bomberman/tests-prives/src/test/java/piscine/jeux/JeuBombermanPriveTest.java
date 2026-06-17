package piscine.jeux;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests privés (robustesse) du cœur de Bomberman. */
@DisplayName("JeuBomberman — tests privés")
class JeuBombermanPriveTest {

    @Test
    @DisplayName("le joueur meurt dans le souffle, survit hors du souffle")
    void mort_et_survie() {
        JeuBomberman mort = new JeuBomberman(Plateau.depuis("....."), new Position(0, 0));
        mort.poserBombe(1, 1);
        mort.tick();                                    // souffle = {(0,0),(0,1)} -> joueur en (0,0) meurt
        assertThat(mort.joueurVivant()).isFalse();

        JeuBomberman survit = new JeuBomberman(Plateau.depuis("....."), new Position(0, 0));
        survit.poserBombe(3, 1);
        survit.deplacer(Direction.DROITE);
        survit.deplacer(Direction.DROITE);
        survit.deplacer(Direction.DROITE);              // (0,3), loin du souffle
        survit.tick();
        survit.tick();
        survit.tick();                                  // explosion en (0,0), portée 1
        assertThat(survit.joueurVivant()).isTrue();
    }

    @Test
    @DisplayName("le souffle s'arrête sur un mur et est absorbé par la première caisse")
    void souffle_bloque() {
        Plateau mur = Plateau.depuis(".#o..");          // mur en (0,1), caisse en (0,2)
        JeuBomberman jeu = new JeuBomberman(mur, new Position(0, 0));
        jeu.poserBombe(1, 5);
        jeu.tick();
        assertThat(jeu.plateau().caseEn(new Position(0, 2)))
            .as("la caisse est protégée par le mur").isEqualTo(Case.CAISSE);

        Plateau deux = Plateau.depuis("..oo.");          // deux caisses en (0,2) et (0,3)
        JeuBomberman jeu2 = new JeuBomberman(deux, new Position(0, 0));
        jeu2.poserBombe(1, 5);
        jeu2.tick();
        assertThat(jeu2.plateau().caseEn(new Position(0, 2))).isEqualTo(Case.VIDE);   // 1re détruite
        assertThat(jeu2.plateau().caseEn(new Position(0, 3))).isEqualTo(Case.CAISSE); // 2e protégée
    }

    @Test
    @DisplayName("une fois mort, le joueur ne bouge plus")
    void aucun_effet_apres_la_mort() {
        JeuBomberman jeu = new JeuBomberman(Plateau.depuis("....."), new Position(0, 0));
        jeu.poserBombe(1, 1);
        jeu.tick();
        assertThat(jeu.joueurVivant()).isFalse();
        Position avant = jeu.positionJoueur();
        jeu.deplacer(Direction.DROITE);
        assertThat(jeu.positionJoueur()).isEqualTo(avant);
    }
}
