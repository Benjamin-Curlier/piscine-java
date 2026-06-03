package etnc.m3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 3.3.2 Vehicule.
 */
@DisplayName("Vehicule — tests publics")
class VehiculeTest {

    @Test
    @DisplayName("Vehicule : decrire de base")
    void vehicule_decrire() {
        assertThat(new Vehicule("Renault", 180).decrire())
            .isEqualTo("Vehicule Renault (180 km/h)");
    }

    @Test
    @DisplayName("Voiture : decrire redéfini")
    void voiture_decrire() {
        assertThat(new Voiture("Peugeot", 200, 5).decrire())
            .isEqualTo("Voiture Peugeot (200 km/h, 5 portes)");
    }

    @Test
    @DisplayName("Moto : decrire redéfini")
    void moto_decrire() {
        assertThat(new Moto("Yamaha", 220).decrire())
            .isEqualTo("Moto Yamaha (220 km/h)");
    }

    @Test
    @DisplayName("accesseurs")
    void accesseurs() {
        Voiture v = new Voiture("Peugeot", 200, 5);
        assertThat(v.getMarque()).isEqualTo("Peugeot");
        assertThat(v.getVitesseMax()).isEqualTo(200);
        assertThat(v.getNbPortes()).isEqualTo(5);
    }
}
