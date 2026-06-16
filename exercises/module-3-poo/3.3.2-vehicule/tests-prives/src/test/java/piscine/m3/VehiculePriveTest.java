package piscine.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests privés de l'exercice 3.3.2 Vehicule — polymorphisme.
 */
@DisplayName("Vehicule — tests privés")
class VehiculePriveTest {

    @Test
    @DisplayName("decrire via le type de base exécute la version Voiture")
    void polymorphe() {
        Vehicule v = new Voiture("Peugeot", 200, 5);
        assertThat(v.decrire()).isEqualTo("Voiture Peugeot (200 km/h, 5 portes)");
    }

    @Test
    @DisplayName("getMarque hérité par Moto")
    void getter_herite() {
        assertThat(new Moto("Yamaha", 220).getMarque()).isEqualTo("Yamaha");
    }

    @Test
    @DisplayName("tableau hétérogène de véhicules")
    void tableau() {
        Vehicule[] parc = { new Voiture("Peugeot", 200, 5), new Moto("Yamaha", 220) };
        assertThat(parc[0].decrire()).isEqualTo("Voiture Peugeot (200 km/h, 5 portes)");
        assertThat(parc[1].decrire()).isEqualTo("Moto Yamaha (220 km/h)");
    }
}
