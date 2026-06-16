package piscine.jeux;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests publics du cœur de Snake (visibles par le stagiaire). */
@DisplayName("JeuSnake — tests publics")
class JeuSnakeTest {

    /** Générateur de pommes déterministe : sert les positions dans l'ordre, puis une case hors-jeu. */
    private static Supplier<Position> pommes(Position... ps) {
        Deque<Position> file = new ArrayDeque<>(List.of(ps));
        return () -> file.isEmpty() ? new Position(-99, -99) : file.poll();
    }

    @Test
    @DisplayName("avancer déplace la tête d'une case dans la direction courante")
    void avancer_deplace_la_tete() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.DROITE, pommes());
        jeu.avancer();
        assertThat(jeu.tete()).isEqualTo(new Position(2, 3));
        assertThat(jeu.longueur()).isEqualTo(1);
        assertThat(jeu.score()).isZero();
        assertThat(jeu.estTermine()).isFalse();
    }

    @Test
    @DisplayName("changerDirection prend effet au pas suivant")
    void changer_direction() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.DROITE, pommes());
        jeu.changerDirection(Direction.BAS);
        assertThat(jeu.direction()).isEqualTo(Direction.BAS);
        jeu.avancer();
        assertThat(jeu.tete()).isEqualTo(new Position(3, 2));
    }

    @Test
    @DisplayName("manger une pomme fait grandir le serpent, +1 au score, et fait apparaître la pomme suivante")
    void manger_fait_grandir() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.DROITE,
                pommes(new Position(2, 3), new Position(0, 4)));
        jeu.avancer();
        assertThat(jeu.score()).isEqualTo(1);
        assertThat(jeu.longueur()).isEqualTo(2);
        assertThat(jeu.pomme()).isEqualTo(new Position(0, 4));
        assertThat(jeu.corps()).containsExactly(new Position(2, 3), new Position(2, 2));
    }

    @Test
    @DisplayName("sortir du plateau termine la partie")
    void mur_termine_la_partie() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 4), Direction.DROITE, pommes());
        jeu.avancer();
        assertThat(jeu.estTermine()).isTrue();
    }

    @Test
    @DisplayName("demi-tour interdit dès que le serpent a un corps")
    void demi_tour_interdit() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.DROITE,
                pommes(new Position(2, 3)));
        jeu.avancer(); // mange : longueur 2
        jeu.changerDirection(Direction.GAUCHE); // opposée de DROITE -> ignorée
        assertThat(jeu.direction()).isEqualTo(Direction.DROITE);
        jeu.avancer();
        assertThat(jeu.tete()).isEqualTo(new Position(2, 4));
        assertThat(jeu.estTermine()).isFalse();
    }

    @Test
    @DisplayName("la pomme initiale provient du générateur")
    void pomme_initiale_du_generateur() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.HAUT,
                pommes(new Position(1, 1)));
        assertThat(jeu.pomme()).isEqualTo(new Position(1, 1));
        assertThat(jeu.score()).isZero();
        assertThat(jeu.longueur()).isEqualTo(1);
    }
}
