package etnc.piscine.moulinette.console.checkers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

class FqcnExtractorTest {

    @Test
    void extrait_fqcn_avec_package(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("HelloWorldTest.java");
        Files.writeString(f, "package etnc.m1;\n\nclass HelloWorldTest {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("etnc.m1.HelloWorldTest");
    }

    @Test
    void extrait_fqcn_sans_package(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("Foo.java");
        Files.writeString(f, "class Foo {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("Foo");
    }

    @Test
    void package_apres_commentaires_et_lignes_vides(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("Bar.java");
        Files.writeString(f, "// commentaire\n\npackage a.b.c;\nclass Bar {}\n");
        assertThat(FqcnExtractor.fqcn(f)).isEqualTo("a.b.c.Bar");
    }

    @Test
    void liste_les_fqcn_dun_repertoire(@TempDir Path dir) throws IOException {
        Path pkg = dir.resolve("src/etnc/m1");
        Files.createDirectories(pkg);
        Files.writeString(pkg.resolve("ATest.java"), "package etnc.m1;\nclass ATest {}\n");
        Files.writeString(pkg.resolve("BTest.java"), "package etnc.m1;\nclass BTest {}\n");
        assertThat(FqcnExtractor.fqcnsUnder(dir.resolve("src")))
            .containsExactlyInAnyOrder("etnc.m1.ATest", "etnc.m1.BTest");
    }
}
