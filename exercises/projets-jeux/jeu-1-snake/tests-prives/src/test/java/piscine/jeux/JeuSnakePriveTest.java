package piscine.jeux;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests privés (robustesse) du cœur de Snake. */
@DisplayName("JeuSnake — tests privés")
class JeuSnakePriveTest {

    private static Supplier<Position> pommes(Position... ps) {
        Deque<Position> file = new ArrayDeque<>(List.of(ps));
        return () -> file.isEmpty() ? new Position(-99, -99) : file.poll();
    }

    @Test
    @DisplayName("entrer dans son propre corps termine la partie")
    void collision_avec_soi_meme() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(2, 2), Direction.DROITE,
                pommes(new Position(2, 3), new Position(2, 4), new Position(3, 4), new Position(3, 3)));
        jeu.avancer();                                  // mange (2,3) -> len 2
        jeu.avancer();                                  // mange (2,4) -> len 3
        jeu.changerDirection(Direction.BAS);
        jeu.avancer();                                  // mange (3,4) -> len 4
        jeu.changerDirection(Direction.GAUCHE);
        jeu.avancer();                                  // mange (3,3) -> len 5
        jeu.changerDirection(Direction.HAUT);
        jeu.avancer();                                  // entre dans le corps en (2,3)
        assertThat(jeu.estTermine()).isTrue();
        assertThat(jeu.score()).isEqualTo(4);
    }

    @Test
    @DisplayName("entrer sur la case que la queue vient de libérer est permis")
    void suivre_sa_queue_est_permis() {
        JeuSnake jeu = new JeuSnake(3, 3, new Position(0, 0), Direction.DROITE,
                pommes(new Position(0, 1), new Position(0, 2), new Position(1, 2)));
        jeu.avancer();                                  // mange (0,1) -> len 2
        jeu.avancer();                                  // mange (0,2) -> len 3
        jeu.changerDirection(Direction.BAS);
        jeu.avancer();                                  // mange (1,2) -> len 4
        jeu.changerDirection(Direction.GAUCHE);
        jeu.avancer();                                  // (1,1), pas de collision
        jeu.changerDirection(Direction.HAUT);
        jeu.avancer();                                  // (0,1) = case libérée par la queue -> permis
        assertThat(jeu.estTermine()).isFalse();
        assertThat(jeu.tete()).isEqualTo(new Position(0, 1));
        assertThat(jeu.longueur()).isEqualTo(4);
    }

    @Test
    @DisplayName("une fois la partie terminée, avancer et changerDirection sont sans effet")
    void aucun_effet_apres_la_fin() {
        JeuSnake jeu = new JeuSnake(5, 5, new Position(0, 0), Direction.GAUCHE, pommes());
        jeu.avancer();                                  // (0,-1) hors plateau -> terminé
        assertThat(jeu.estTermine()).isTrue();
        Position tete = jeu.tete();
        jeu.changerDirection(Direction.BAS);
        jeu.avancer();
        assertThat(jeu.tete()).isEqualTo(tete);
        assertThat(jeu.direction()).isEqualTo(Direction.GAUCHE);
    }
}
