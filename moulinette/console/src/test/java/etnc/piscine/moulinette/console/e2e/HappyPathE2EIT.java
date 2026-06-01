package etnc.piscine.moulinette.console.e2e;

import etnc.piscine.moulinette.console.Mode;
import etnc.piscine.moulinette.console.commands.CommandRegistry;
import etnc.piscine.moulinette.console.git.ProcessGitClient;
import etnc.piscine.moulinette.console.repl.*;
import etnc.piscine.moulinette.console.trigger.MoulinetteRunner;
import etnc.piscine.moulinette.console.trigger.SubmissionTrigger;
import etnc.piscine.moulinette.console.workspace.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("e2e")
class HappyPathE2EIT {

    @Test
    void rendu_complet_exo_1_1_1_produit_un_rapport_ok(@TempDir Path tmp) throws Exception {
        // 1. Faux repo Piscine avec l'exo 1.1.1
        Path piscine = tmp.resolve("piscine-etnc");
        Path exoDir = piscine.resolve("exercises/module-1-fondamentaux/1.1.1-hello-world");
        Files.createDirectories(exoDir.resolve("starter"));
        Files.writeString(exoDir.resolve("metadata.yml"), """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Files.writeString(exoDir.resolve("starter/Hello.java"),
            "class Hello{public static void main(String[] a){}}");

        // 2. init workspace
        Path dest = tmp.resolve("piscine-curlier");
        var initializer = new LocalWorkspaceInitializer(new ProcessGitClient());
        Workspace ws = initializer.init(new InitRequest("curlier", dest, piscine, "module-1-fondamentaux"));

        // 3. REPL avec un MoulinetteRunner toujours OK (le pipeline réel est testé ailleurs)
        var catalog = ExerciseCatalog.scan(piscine.resolve("exercises"));
        MoulinetteRunner runner = (sg, repo) -> new MoulinetteRunner.GroupReport(
            sg,
            List.of(new MoulinetteRunner.ExoOutcome("1.1.1", true, "")),
            false,
            ws.repoRoot().resolve(".piscine/reports/" + sg + "-fake.md")
        );
        var trigger = new SubmissionTrigger(runner);
        var ctx = new ReplContext(ws.repoRoot(), new ProcessGitClient(), catalog, Mode.LOCAL);

        String script = String.join("\n",
            "submit-start 1.1",
            "git add .",
            "git commit -m \"rendu 1.1.1\"",
            "git push origin rendu/1.1",
            "exit", ""
        );
        var sw = new StringWriter();
        var io = new ReplIo(new StringReader(script), sw);

        new Repl(ctx, CommandRegistry.defaults(trigger), io).run();

        String out = sw.toString();
        assertThat(out).contains("rendu/1.1");
        assertThat(out).contains("✓ OK");
        assertThat(out).contains("Rapport");
    }
}
