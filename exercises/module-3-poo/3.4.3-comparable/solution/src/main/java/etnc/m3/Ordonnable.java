package etnc.m3;

/**
 * Exercice 3.4.3 — Ordonnable : contrat de comparaison maison (FOURNI).
 *
 * <p>comparerA renvoie un entier negatif / nul / positif selon que cet objet est
 * avant / egal / apres l'autre.</p>
 */
public interface Ordonnable {
    int comparerA(Ordonnable autre);
}
