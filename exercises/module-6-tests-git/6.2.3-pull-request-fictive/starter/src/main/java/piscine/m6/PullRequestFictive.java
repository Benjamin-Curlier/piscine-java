package piscine.m6;

import java.io.IOException;

/**
 * À COMPLÉTER. Simulez le cycle d'une Pull Request en local : créer une branche, y commiter
 * un changement, la fusionner dans {@code main}, et rédiger un fichier {@code PULL_REQUEST.md}
 * décrivant la PR. Le dépôt est déjà initialisé avec un commit de départ sur {@code main}.
 */
public class PullRequestFictive {

    /**
     * Doit :
     * <ol>
     *   <li>créer et basculer sur une branche {@code feature/ajoute-licence} ;</li>
     *   <li>créer un fichier {@code LICENSE}, {@code add} + {@code commit}
     *       (message « Ajoute le fichier LICENSE ») ;</li>
     *   <li>revenir sur {@code main} et **fusionner** la branche ;</li>
     *   <li>écrire {@code PULL_REQUEST.md} avec un titre, une section {@code ## Description} et
     *       une section {@code ## Checklist de revue}.</li>
     * </ol>
     */
    public void preparerPullRequest(GitCommandes git) throws IOException {
        // TODO : git switch -c feature/ajoute-licence ; créer LICENSE ; add + commit ;
        // TODO : git switch main ; git merge feature/ajoute-licence ; écrire PULL_REQUEST.md.
        throw new UnsupportedOperationException("À implémenter : preparerPullRequest");
    }
}
