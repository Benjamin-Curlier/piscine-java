package piscine.moulinette.console.gamification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Moteur de gamification (pur, sans effet de bord) : à partir des exercices validés et du
 * catalogue, calcule l'XP, le niveau, la progression et les badges débloqués.
 */
public final class Gamification {

    private Gamification() { }

    /** Seuils d'XP (croissants) ouvrant chaque niveau, et leurs titres. */
    private static final int[] PALIERS = {0, 100, 250, 500, 850, 1300};
    private static final String[] TITRES = {
        "Débutant", "Apprenti", "Initié", "Confirmé", "Expert", "Maître"
    };

    public static Profil evaluer(Set<String> exosValides, List<Exo> tousLesExos) {
        int xp = 0;
        int valides = 0;
        for (Exo e : tousLesExos) {
            if (exosValides.contains(e.id())) {
                xp += e.difficulte().xp();
                valides++;
            }
        }
        int niveau = niveauPour(xp);
        String titre = TITRES[niveau - 1];
        int xpProchain = niveau < PALIERS.length ? PALIERS[niveau] : -1;
        int pct = tousLesExos.isEmpty() ? 0
            : (int) Math.round(100.0 * valides / tousLesExos.size());
        return new Profil(xp, niveau, titre, xpProchain, pct,
            badges(exosValides, tousLesExos, valides, pct));
    }

    private static int niveauPour(int xp) {
        int niveau = 1;
        for (int i = 0; i < PALIERS.length; i++) {
            if (xp >= PALIERS[i]) {
                niveau = i + 1;
            }
        }
        return niveau;
    }

    private static List<Badge> badges(Set<String> valides, List<Exo> tous, int nbValides, int pct) {
        List<Badge> out = new ArrayList<>();
        if (nbValides >= 1) {
            out.add(new Badge("premier-sang", "Premier sang", "Valider ton premier exercice."));
        }
        if (nbValides >= 10) {
            out.add(new Badge("assidu", "Assidu·e", "Valider 10 exercices."));
        }
        for (int module : modulesPresents(tous)) {
            boolean complet = tous.stream()
                .filter(e -> e.module() == module)
                .allMatch(e -> valides.contains(e.id()));
            if (complet) {
                out.add(new Badge("module-" + module, "Maître du module " + module,
                    "Valider tous les exercices du module " + module + "."));
            }
        }
        if (pct >= 50 && pct < 100) {
            out.add(new Badge("mi-parcours", "Mi-parcours", "Valider la moitié de la Piscine."));
        }
        if (pct >= 100 && !tous.isEmpty()) {
            out.add(new Badge("diplome", "Diplômé·e 🎓", "Valider TOUS les exercices. Bravo !"));
        }
        return out;
    }

    private static Set<Integer> modulesPresents(List<Exo> tous) {
        Set<Integer> modules = new TreeSet<>();
        for (Exo e : tous) {
            modules.add(e.module());
        }
        return modules;
    }
}
