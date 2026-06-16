package piscine.m6;

/** Implémentation correcte de référence (identique au starter). */
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
        if (note >= 10) {
            return "Passable";
        }
        return "Insuffisant";
    }
}
