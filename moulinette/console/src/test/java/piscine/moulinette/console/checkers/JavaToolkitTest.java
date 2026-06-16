package piscine.moulinette.console.checkers;

import piscine.moulinette.runner.ProcessRunner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaToolkitTest {

    @Test
    void tooling_classpath_non_vide() {
        var tk = new JavaToolkit(new ProcessRunner());
        // En dev (hors jar) : retombe sur java.class.path, jamais vide.
        assertThat(tk.toolingClasspath()).isNotBlank();
    }

    @Test
    void java_exe_pointe_vers_un_binaire_java() {
        var tk = new JavaToolkit(new ProcessRunner());
        assertThat(tk.javaExe()).contains("java");
    }
}
