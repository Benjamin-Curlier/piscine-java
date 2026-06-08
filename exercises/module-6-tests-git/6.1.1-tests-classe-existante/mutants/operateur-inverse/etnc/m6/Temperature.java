package etnc.m6;

// MUTANT « operateur-inverse » : inverse le rapport (× 5 / 9 au lieu de × 9 / 5).
// Passe à 0 °C (donne 32) mais échoue à 100 °C (donne 87 au lieu de 212).
public class Temperature {

    public int celsiusVersFahrenheit(int celsius) {
        return celsius * 5 / 9 + 32;
    }

    public boolean estPositive(int celsius) {
        return celsius > 0;
    }
}
