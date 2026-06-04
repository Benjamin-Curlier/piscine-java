package etnc.m4;

import java.util.List;

/**
 * Exercice 4.1.1 — Annuaire militaire : gestion d'une Map indicatif -> nom.
 *
 * <p>Completez sans changer les signatures publiques.</p>
 */
public class Annuaire {

    // TODO : declarer le champ prive final Map<String,String> annuaire initialise avec new HashMap<>()

    public void enregistrer(String indicatif, String nom) {
        // TODO : associer nom a indicatif dans la map
    }

    public String rechercher(String indicatif) {
        return "Inconnu"; // TODO : retourner le nom ou "Inconnu" si absent
    }

    public boolean supprimer(String indicatif) {
        return false; // TODO : retirer l'entree et renvoyer true, ou false si absent
    }

    public int taille() {
        return 0; // TODO : retourner le nombre d'entrees
    }

    public List<String> indicatifsTries() {
        return List.of(); // TODO : retourner la liste des indicatifs tries alphabetiquement
    }
}
