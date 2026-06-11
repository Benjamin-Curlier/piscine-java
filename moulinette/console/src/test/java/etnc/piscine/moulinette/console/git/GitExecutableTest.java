package etnc.piscine.moulinette.console.git;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GitExecutableTest {

    @AfterEach
    void cleanup() {
        System.clearProperty("piscine.git");
    }

    @Test
    void par_defaut_git_du_path() {
        assertThat(ProcessGitClient.gitExecutable()).isEqualTo("git");
    }

    @Test
    void la_propriete_systeme_piscine_git_prime() {
        System.setProperty("piscine.git", "C:\\apps\\mingit\\cmd\\git.exe");
        assertThat(ProcessGitClient.gitExecutable()).isEqualTo("C:\\apps\\mingit\\cmd\\git.exe");
    }
}
