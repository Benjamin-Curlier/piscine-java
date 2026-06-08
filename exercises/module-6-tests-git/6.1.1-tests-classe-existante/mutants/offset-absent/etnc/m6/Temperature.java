package etnc.m6;

// MUTANT « offset-absent » : oublie le + 32 dans la conversion.
// Tué par un test sur 0 °C (attendu 32, ce mutant renvoie 0).
public class Temperature {

    public int celsiusVersFahrenheit(int celsius) {
        return celsius * 9 / 5;
    }

    public boolean estPositive(int celsius) {
        return celsius > 0;
    }
}
