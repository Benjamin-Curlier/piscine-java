package piscine.moulinette.console.trigger;

import piscine.moulinette.console.gamification.AdaptateurCatalogue;
import piscine.moulinette.console.gamification.Badge;
import piscine.moulinette.console.gamification.Exo;
import piscine.moulinette.console.gamification.Gamification;
import piscine.moulinette.console.gamification.Profil;
import piscine.moulinette.console.git.RefUpdate;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SubmissionTrigger {
    private static final Pattern RENDU_REF = Pattern.compile("refs/heads/rendu/(\\d+\\.\\d+)");

    private final MoulinetteRunner runner;

    public SubmissionTrigger(MoulinetteRunner runner) { this.runner = runner; }

    /** Appelée par PushCommand après un push réussi. Retourne le texte à afficher (ou null). */
    public String onPushSucceeded(ReplContext ctx) {
        for (RefUpdate ru : ctx.git().lastPushRefs(ctx.repoRoot())) {
            Matcher m = RENDU_REF.matcher(ru.ref());
            if (m.matches()) {
                String sg = m.group(1);
                // On photographie la gamification avant/après le run pour annoncer XP, niveau et
                // badges fraîchement débloqués directement dans le retour du submit (le moteur est
                // pur ; runGroup met à jour progress.json entre les deux lectures).
                Path progress = ctx.repoRoot().resolve(".piscine/progress.json");
                List<Exo> exos = ctx.catalog() == null ? List.of()
                    : AdaptateurCatalogue.exos(ctx.catalog());
                Profil avant = exos.isEmpty() ? null
                    : Gamification.evaluer(MoulinetteRunner.Default.lireProgression(progress), exos);
                GroupReport rep = runner.runGroup(sg, ctx.repoRoot());
                Profil apres = exos.isEmpty() ? null
                    : Gamification.evaluer(MoulinetteRunner.Default.lireProgression(progress), exos);
                return formatReport(sg, rep) + resumeGamification(avant, apres);
            }
        }
        return null;
    }

    private static String formatReport(String sg, GroupReport rep) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[console] Push détecté sur rendu/").append(sg)
          .append(" → lancement moulinette sur sous-groupe ").append(sg).append("\n");
        for (ExoOutcome o : rep.outcomes()) {
            sb.append("[console] ▶ Exo ").append(o.exoId()).append("  ")
              .append(o.ok() ? "✓ OK" : "✗ ÉCHEC").append("\n");
            if (!o.ok() && !o.message().isBlank()) sb.append("    ").append(o.message());
        }
        sb.append("[console] Rapport : ").append(rep.reportPath()).append("\n");
        if (rep.tousReussis()) {
            sb.append("[console] 🎉 Sous-groupe complet — bravo ! Tu peux passer au suivant.\n");
        } else {
            sb.append("[console] Corrige les exos en ✗ ci-dessus puis re-pousse "
                    + "(la progression se débloque dans l'ordre).\n");
        }
        return sb.toString();
    }

    /**
     * Annonce XP gagnée, montée de niveau et nouveaux badges après un rendu. Renvoie une chaîne
     * vide si rien n'a été débloqué (aucun nouvel exo validé, niveau max sans progrès, etc.) pour
     * ne pas polluer le retour. Pur et déterministe — testé directement.
     */
    static String resumeGamification(Profil avant, Profil apres) {
        if (avant == null || apres == null || apres.xp() <= avant.xp()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[console] 🎮 +").append(apres.xp() - avant.xp())
          .append(" XP (total ").append(apres.xp()).append(')');
        if (apres.niveau() > avant.niveau()) {
            sb.append(" — Niveau ").append(apres.niveau()).append(" : ")
              .append(apres.titre()).append(" débloqué !");
        }
        sb.append('\n');
        Set<String> dejaObtenus = new HashSet<>();
        for (Badge b : avant.badges()) dejaObtenus.add(b.id());
        for (Badge b : apres.badges()) {
            if (dejaObtenus.add(b.id())) {
                sb.append("[console] 🏅 Nouveau badge : ").append(b.nom())
                  .append(" — ").append(b.description()).append('\n');
            }
        }
        sb.append("[console] Tape `profil` pour voir ta progression complète.\n");
        return sb.toString();
    }
}
