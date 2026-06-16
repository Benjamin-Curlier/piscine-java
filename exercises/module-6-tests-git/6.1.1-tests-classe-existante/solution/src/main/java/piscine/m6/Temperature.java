package piscine.m6;

/** Implémentation correcte de référence (identique au starter). */
public class Temperature {

    public int celsiusVersFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }

    public boolean estPositive(int celsius) {
        return celsius > 0;
    }
}
