package etnc.m6;

// MUTANT « seuil-tres-bien » : seuil Très bien décalé (>= 15 au lieu de >= 16).
// Tué par un test affirmant que mention(15) == "Bien" (ce mutant renvoie "Très bien").
public class Classement {

    public String mention(int note) {
        if (note >= 15) {
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
