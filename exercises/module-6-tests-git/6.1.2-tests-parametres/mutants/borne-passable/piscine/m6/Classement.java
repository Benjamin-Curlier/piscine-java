package piscine.m6;

// MUTANT « borne-passable » : seuil Passable décalé (>= 11 au lieu de >= 10).
// Tué par un test affirmant que mention(10) == "Passable" (ce mutant renvoie "Insuffisant").
public class Classement {

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
        if (note >= 11) {
            return "Passable";
        }
        return "Insuffisant";
    }
}
