package piscine.m6;

// MUTANT « borne-zero » : se trompe de frontière (>= 0 au lieu de > 0).
// Tué par un test affirmant que estPositive(0) est false (ce mutant renvoie true).
public class Temperature {

    public int celsiusVersFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }

    public boolean estPositive(int celsius) {
        return celsius >= 0;
    }
}
