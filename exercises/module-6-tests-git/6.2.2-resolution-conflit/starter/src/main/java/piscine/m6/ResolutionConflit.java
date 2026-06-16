package piscine.m6;

import java.io.IOException;

/**
 * À COMPLÉTER. Le dépôt passé en paramètre est dans un état de **conflit de fusion** :
 * une tentative de {@code git merge} a laissé des marqueurs de conflit dans {@code README.md}.
 * Votre travail : résoudre le conflit et conclure la fusion.
 */
public class ResolutionConflit {

    /**
     * Doit :
     * <ol>
     *   <li>lire {@code README.md} (qui contient des marqueurs {@code <<<<<<<}, {@code =======},
     *       {@code >>>>>>>}) ;</li>
     *   <li>retirer les lignes de marqueurs en gardant les **deux** contributions (les lignes
     *       ajoutées de chaque côté) ;</li>
     *   <li>réécrire le fichier, puis {@code git add README.md} et {@code git commit} pour
     *       conclure la fusion.</li>
     * </ol>
     */
    public void resoudreConflit(GitCommandes git) throws IOException {
        // TODO : lire README.md (git.repo().resolve("README.md")), retirer les 3 types de
        // TODO : marqueurs, garder import ET export, réécrire, puis git add + git commit.
        throw new UnsupportedOperationException("À implémenter : resoudreConflit");
    }
}
