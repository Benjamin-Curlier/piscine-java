package piscine.m6;

/**
 * Classe FOURNIE et CORRECTE. Ne la modifiez pas : votre travail est d'écrire
 * la suite de tests qui la valide (voir {@code src/test/java/piscine/m6/TemperatureTest.java}).
 */
public class Temperature {

    /** Convertit une température en degrés Celsius vers les degrés Fahrenheit. */
    public int celsiusVersFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }

    /** Vrai si la température (en °C) est strictement positive. */
    public boolean estPositive(int celsius) {
        return celsius > 0;
    }
}
