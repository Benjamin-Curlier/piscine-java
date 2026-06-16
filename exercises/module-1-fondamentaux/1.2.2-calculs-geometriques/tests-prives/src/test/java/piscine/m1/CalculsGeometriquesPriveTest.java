package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests prives de l'exercice 1.2.2 Calculs geometriques.
 *
 * Le rayon 1 leve l'ambiguite du rayon 2 (ou aire et perimetre coincident) :
 * aire = pi, perimetre = 2*pi, deux valeurs distinctes.
 */
@DisplayName("Calculs géométriques — tests privés")
class CalculsGeometriquesPriveTest {

    private static final String NL = System.lineSeparator();

    private static String executer(String entree) {
        String[] sortie = new String[1];
        CaptureEntree.avecEntree(entree,
            () -> sortie[0] = CaptureSortie.capturer(() -> CalculsGeometriques.main(new String[]{})));
        return sortie[0];
    }

    private static String attendu(double rayon) {
        double aire = Math.PI * rayon * rayon;
        double perimetre = 2 * Math.PI * rayon;
        return "Rayon du cercle ?" + NL
            + "Aire : " + aire + NL
            + "Périmètre : " + perimetre + NL;
    }

    @Test
    @DisplayName("rayon 1 : aire = pi, périmètre = 2*pi (valeurs distinctes)")
    void rayon_1() {
        String sortie = executer("1" + NL);
        assertThat(sortie).isEqualTo(attendu(1.0));
        assertThat(sortie)
            .as("Aire et périmètre doivent être différents pour un rayon de 1")
            .contains("Aire : " + Math.PI)
            .contains("Périmètre : " + (2 * Math.PI));
    }

    @Test
    @DisplayName("fonctionne pour un rayon de 3")
    void rayon_3() {
        assertThat(executer("3" + NL)).isEqualTo(attendu(3.0));
    }
}
