package piscine.m6;

import java.io.IOException;

/**
 * À COMPLÉTER. Le dépôt passé en paramètre est déjà initialisé (un {@code git init} et une
 * identité git ont été configurés). Votre travail : enregistrer DEUX fichiers en DEUX commits
 * atomiques (un fichier par commit), avec des messages clairs.
 */
public class CommitPropre {

    /**
     * Doit produire exactement cet historique (du plus ancien au plus récent) :
     * <ol>
     *   <li>créer {@code notes.txt} (contenu « première note »), puis {@code add} + {@code commit}
     *       avec le message « Ajoute les notes » ;</li>
     *   <li>créer {@code liste.txt} (contenu « première tâche »), puis {@code add} + {@code commit}
     *       avec le message « Ajoute la liste ».</li>
     * </ol>
     * Utilisez {@code git.repo()} pour localiser le dépôt et {@code git.run(...)} pour les commandes.
     */
    public void creerHistorique(GitCommandes git) throws IOException {
        // TODO : créez notes.txt, puis git add notes.txt, puis git commit -m "Ajoute les notes".
        // TODO : créez liste.txt, puis git add liste.txt, puis git commit -m "Ajoute la liste".
        throw new UnsupportedOperationException("À implémenter : creerHistorique");
    }
}
