package piscine.moulinette.console.trigger;

import piscine.moulinette.console.git.RefUpdate;
import piscine.moulinette.console.repl.ReplContext;
import piscine.moulinette.console.trigger.MoulinetteRunner.ExoOutcome;
import piscine.moulinette.console.trigger.MoulinetteRunner.GroupReport;

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
                GroupReport rep = runner.runGroup(sg, ctx.repoRoot());
                return formatReport(sg, rep);
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
}
