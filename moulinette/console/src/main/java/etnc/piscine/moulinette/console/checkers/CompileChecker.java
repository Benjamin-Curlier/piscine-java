package etnc.piscine.moulinette.console.checkers;

import etnc.piscine.moulinette.framework.CheckResult;
import etnc.piscine.moulinette.framework.Checker;
import etnc.piscine.moulinette.framework.CheckerContext;
import etnc.piscine.moulinette.runner.ProcessResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Compile le code stagiaire (sans les tests). Premier gardien de la chaîne. */
public final class CompileChecker implements Checker {

    private final JavaToolkit toolkit;

    public CompileChecker(JavaToolkit toolkit) { this.toolkit = toolkit; }

    @Override public String id() { return "compile"; }

    @Override
    public CheckResult check(CheckerContext ctx) {
        Path mainSrc = ctx.renduPath().resolve("starter/src/main/java");
        Path out = ctx.renduPath().resolve(".piscine/build/classes-main");
        try {
            ProcessResult r = toolkit.compile(List.of(mainSrc), out, toolkit.toolingClasspath());
            if (r.exitCode() == 0) return CheckResult.ok();
            return CheckResult.fail(
                "Ton code ne compile pas :\n" + tronquer(r.stderr()),
                "Corrige les erreurs de compilation ci-dessus avant de continuer.");
        } catch (IOException e) {
            return CheckResult.error("Échec technique de la compilation : " + e.getMessage());
        }
    }

    static String tronquer(String s) {
        String[] lignes = s.split("\n");
        if (lignes.length <= 30) return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 30; i++) sb.append(lignes[i]).append('\n');
        sb.append("… (").append(lignes.length - 30).append(" lignes de plus)");
        return sb.toString();
    }
}
