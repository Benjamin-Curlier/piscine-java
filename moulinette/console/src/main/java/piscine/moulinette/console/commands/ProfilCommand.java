package piscine.moulinette.console.commands;

import java.util.List;
import java.util.Set;
import piscine.moulinette.console.gamification.AdaptateurCatalogue;
import piscine.moulinette.console.gamification.Badge;
import piscine.moulinette.console.gamification.Gamification;
import piscine.moulinette.console.gamification.Profil;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.MoulinetteRunner;

/** Affiche le profil de jeu du stagiaire : XP, niveau, progression et badges. */
public final class ProfilCommand implements Command {

    @Override public String name() { return "profil"; }

    @Override public String shortHelp() {
        return "profil — affiche ton XP, ton niveau et tes badges";
    }

    @Override
    public CommandResult execute(ReplContext ctx, List<String> args) {
        if (ctx.catalog() == null) {
            return CommandResult.error("Catalogue indisponible.\n");
        }
        Set<String> valides = MoulinetteRunner.Default.lireProgression(
            ctx.repoRoot().resolve(".piscine/progress.json"));
        Profil p = Gamification.evaluer(valides, AdaptateurCatalogue.exos(ctx.catalog()));
        return CommandResult.ok(format(p));
    }

    static String format(Profil p) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("  ★ Niveau ").append(p.niveau()).append(" — ").append(p.titre())
          .append("    ").append(p.xp()).append(" XP");
        if (p.xpProchainNiveau() >= 0) {
            sb.append("  (").append(p.xpProchainNiveau() - p.xp()).append(" XP avant le niveau suivant)");
        }
        sb.append('\n');
        sb.append("  ").append(barre(p.progressionPourcent())).append("  ")
          .append(p.progressionPourcent()).append("% des exercices validés\n");
        if (p.badges().isEmpty()) {
            sb.append("  Aucun badge — valide ton premier exercice pour décrocher « Premier sang » !\n");
        } else {
            sb.append("  Badges (").append(p.badges().size()).append(") :\n");
            for (Badge b : p.badges()) {
                sb.append("    🏅 ").append(b.nom()).append(" — ").append(b.description()).append('\n');
            }
        }
        return sb.toString();
    }

    /** Barre de progression de 20 segments. */
    private static String barre(int pourcent) {
        int rempli = Math.round(pourcent / 5f);
        return "[" + "█".repeat(rempli) + "·".repeat(20 - rempli) + "]";
    }
}
