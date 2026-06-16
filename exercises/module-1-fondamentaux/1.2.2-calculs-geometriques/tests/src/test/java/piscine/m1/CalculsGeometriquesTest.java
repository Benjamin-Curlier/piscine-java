package piscine.m1;

import piscine.util.CaptureEntree;
import piscine.util.CaptureSortie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests publics de l'exercice 1.2.2 Calculs geometriques.
 *
 * L'attendu est construit avec Math.PI et la meme arithmetique que la solution,
 * pour ne pas dependre de la representation exacte des nombres a virgule.
 */
@DisplayName("Calculs géométriques — tests publics")
class CalculsGeometriquesTest {

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
    @DisplayName("calcule aire et périmètre pour un rayon de 2")
    void rayon_2() {
        assertThat(executer("2" + NL))
            .as("Aire = pi*4, périmètre = 4*pi")
            .isEqualTo(attendu(2.0));
    }
}
