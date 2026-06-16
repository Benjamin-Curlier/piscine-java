package piscine.m6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * À COMPLÉTER. Un premier test vous est donné en modèle ; ajoutez les autres
 * (voir sujet.md). Vos tests doivent distinguer une implémentation correcte
 * d'une implémentation buggée — couvrez les cas limites (0 °C, la frontière 0).
 */
class TemperatureTest {

    private final Temperature temperature = new Temperature();

    @Test
    void zeroCelsiusVaut32Fahrenheit() {
        assertThat(temperature.celsiusVersFahrenheit(0)).isEqualTo(32);
    }

    // TODO : 100 °C doit valoir 212 °F.
    // TODO : estPositive(0) doit être false (0 n'est pas strictement positif).
    // TODO : estPositive sur une valeur positive doit être true.
    // TODO : estPositive sur une valeur négative doit être false.
}
