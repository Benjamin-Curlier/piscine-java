package etnc.m4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Exercice 4.1.1 — Annuaire militaire (solution de reference). */
public class Annuaire {

    private final Map<String, String> annuaire = new HashMap<>();

    public void enregistrer(String indicatif, String nom) {
        annuaire.put(indicatif, nom);
    }

    public String rechercher(String indicatif) {
        return annuaire.getOrDefault(indicatif, "Inconnu");
    }

    public boolean supprimer(String indicatif) {
        return annuaire.remove(indicatif) != null;
    }

    public int taille() {
        return annuaire.size();
    }

    public List<String> indicatifsTries() {
        List<String> liste = new ArrayList<>(annuaire.keySet());
        Collections.sort(liste);
        return liste;
    }
}
