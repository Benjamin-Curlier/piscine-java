package piscine.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Suite de tests MODÈLE. Elle passe sur l'implémentation correcte et tue les trois
 * mutants : {@code offset-absent} (test à 0 °C), {@code operateur-inverse} (test à 100 °C),
 * {@code borne-zero} (test estPositive(0) == false).
 */
class TemperatureTest {

    private final Temperature temperature = new Temperature();

    @Test
    void zeroCelsiusVaut32Fahrenheit() {
        assertThat(temperature.celsiusVersFahrenheit(0)).isEqualTo(32);
    }

    @Test
    void centCelsiusVaut212Fahrenheit() {
        assertThat(temperature.celsiusVersFahrenheit(100)).isEqualTo(212);
    }

    @Test
    void zeroNEstPasStrictementPositif() {
        assertThat(temperature.estPositive(0)).isFalse();
    }

    @Test
    void uneTemperaturePositiveEstDetectee() {
        assertThat(temperature.estPositive(5)).isTrue();
    }

    @Test
    void uneTemperatureNegativeNEstPasPositive() {
        assertThat(temperature.estPositive(-3)).isFalse();
    }
}
