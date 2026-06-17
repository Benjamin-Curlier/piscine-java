package piscine.jeux;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests publics du cœur de Bomberman (carte : '#'=mur, 'o'=caisse, '.'=vide). */
@DisplayName("JeuBomberman — tests publics")
class JeuBombermanTest {

    @Test
    @DisplayName("le joueur se déplace sur une case vide, mais est bloqué par un mur, une caisse, ou le bord")
    void deplacement() {
        Plateau p = Plateau.depuis(
            "...",
            ".#.",
            "o..");
        JeuBomberman jeu = new JeuBomberman(p, new Position(0, 0));

        jeu.deplacer(Direction.DROITE);                 // (0,1) vide
        assertThat(jeu.positionJoueur()).isEqualTo(new Position(0, 1));
        jeu.deplacer(Direction.BAS);                    // (1,1) mur -> bloqué
        assertThat(jeu.positionJoueur()).isEqualTo(new Position(0, 1));
        jeu.deplacer(Direction.HAUT);                   // hors plateau -> bloqué
        assertThat(jeu.positionJoueur()).isEqualTo(new Position(0, 1));

        JeuBomberman jeu2 = new JeuBomberman(p, new Position(2, 1));
        jeu2.deplacer(Direction.GAUCHE);                // (2,0) caisse -> bloqué
        assertThat(jeu2.positionJoueur()).isEqualTo(new Position(2, 1));
    }

    @Test
    @DisplayName("une bombe explose après `compteur` ticks et disparaît")
    void bombe_explose_au_bon_moment() {
        JeuBomberman jeu = new JeuBomberman(Plateau.depuis("....."), new Position(0, 4));
        jeu.poserBombe(2, 1);
        assertThat(jeu.bombes()).hasSize(1);
        jeu.tick();                                     // 2 -> 1
        assertThat(jeu.bombes()).hasSize(1);
        jeu.tick();                                     // 1 -> 0 : explose
        assertThat(jeu.bombes()).isEmpty();
    }

    @Test
    @DisplayName("le souffle détruit une caisse à portée")
    void souffle_detruit_caisse() {
        Plateau p = Plateau.depuis("..o..");            // caisse en (0,2)
        JeuBomberman jeu = new JeuBomberman(p, new Position(0, 0));
        jeu.poserBombe(1, 5);
        jeu.tick();
        assertThat(jeu.plateau().caseEn(new Position(0, 2))).isEqualTo(Case.VIDE);
    }
}
