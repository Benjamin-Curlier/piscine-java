package etnc.m4;

/**
 * Exercice 4.1.2 — Utilisateur : egalite logique a definir.
 *
 * <p>Les champs, le constructeur et les accesseurs sont fournis. A vous d'ecrire
 * equals et hashCode pour que deux utilisateurs de memes nom et matricule soient
 * consideres egaux. Ne changez pas les signatures publiques.</p>
 */
public class Utilisateur {

    private final String nom;
    private final int matricule;

    public Utilisateur(String nom, int matricule) {
        this.nom = nom;
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public int getMatricule() {
        return matricule;
    }

    @Override
    public boolean equals(Object o) {
        // TODO : meme objet -> true ; verifier le type (o instanceof Utilisateur u) ;
        // comparer matricule ET nom (indice : Objects.equals pour le nom)
        return false; // TODO
    }

    @Override
    public int hashCode() {
        // TODO : combiner les memes champs que equals (indice : Objects.hash)
        return 0; // TODO
    }
}
