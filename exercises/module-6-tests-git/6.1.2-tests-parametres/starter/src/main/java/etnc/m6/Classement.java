package etnc.m6;

/**
 * Classe FOURNIE et CORRECTE. Ne la modifiez pas : écrivez la suite de tests qui la
 * valide (voir {@code src/test/java/etnc/m6/ClassementTest.java}).
 */
public class Classement {

    /** Attribue une mention à une note sur 20. */
    public String mention(int note) {
        if (note >= 16) {
            return "Très bien";
        }
        if (note >= 14) {
            return "Bien";
        }
        if (note >= 12) {
            return "Assez bien";
        }
        if (note >= 10) {
            return "Passable";
        }
        return "Insuffisant";
    }
}
