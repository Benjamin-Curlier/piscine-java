package piscine.m6;

// MUTANT « retour-constant » : renvoie toujours la même mention.
// Tué par n'importe quel test dont la mention attendue n'est pas "Passable".
public class Classement {

    public String mention(int note) {
        return "Passable";
    }
}
