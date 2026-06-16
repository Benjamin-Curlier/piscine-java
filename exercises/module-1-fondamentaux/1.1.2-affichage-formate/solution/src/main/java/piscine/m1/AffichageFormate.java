package piscine.m1;

/**
 * Exercice 1.1.2 — Affichage formate (solution de reference).
 *
 * <p>Montre comment composer une sortie multi-lignes lisible a partir de
 * variables, en combinant texte fixe et concatenation.</p>
 */
public class AffichageFormate {

    public static void main(String[] args) {
        String nom = "Martin";
        String niveau = "Confirmé";
        int age = 29;

        // Les libelles sont alignes sur 7 caracteres avant les deux-points :
        // la fiche reste lisible quel que soit le libelle.
        System.out.println("=== Fiche membre ===");
        System.out.println("Nom    : " + nom);
        System.out.println("Niveau : " + niveau);
        System.out.println("Age    : " + age + " ans");
    }
}
