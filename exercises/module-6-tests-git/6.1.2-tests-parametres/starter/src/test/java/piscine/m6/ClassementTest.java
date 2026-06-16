package piscine.m6;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * À COMPLÉTER. Un test paramétré amorcé vous est donné ; ajoutez des lignes à
 * {@code @CsvSource} pour couvrir CHAQUE palier et ses frontières (10, 12, 14, 16 et
 * les valeurs juste en dessous). Vos tests doivent détecter une frontière décalée.
 */
class ClassementTest {

    @ParameterizedTest
    @CsvSource({
        "20, Très bien",
        "9, Insuffisant"
        // TODO : ajoute 16→Très bien, 15→Bien, 14→Bien, 13→Assez bien, 12→Assez bien,
        // TODO : 11→Passable, 10→Passable. Couvre chaque frontière.
    })
    void mentionSelonLaNote(int note, String attendue) {
        assertThat(new Classement().mention(note)).isEqualTo(attendue);
    }
}
