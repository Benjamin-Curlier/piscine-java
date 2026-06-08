package etnc.m6;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Suite MODÈLE. Couvre chaque palier et ses frontières. Tue les trois mutants :
 * {@code borne-passable} (10 mal classé), {@code seuil-tres-bien} (15 mal classé),
 * {@code retour-constant} (mention figée).
 */
class ClassementTest {

    @ParameterizedTest
    @CsvSource({
        "20, Très bien",
        "16, Très bien",
        "15, Bien",
        "14, Bien",
        "13, Assez bien",
        "12, Assez bien",
        "11, Passable",
        "10, Passable",
        "9, Insuffisant",
        "0, Insuffisant"
    })
    void mentionSelonLaNote(int note, String attendue) {
        assertThat(new Classement().mention(note)).isEqualTo(attendue);
    }
}
