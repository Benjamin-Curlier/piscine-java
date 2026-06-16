package piscine.moulinette.console.checkers;

import piscine.moulinette.runner.ProcessResult;
import piscine.moulinette.runner.ProcessRunner;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/** Façade d'outillage Java (javac / JUnit ConsoleLauncher / Checkstyle) au-dessus de {@link ProcessRunner}. */
public final class JavaToolkit {

    private final ProcessRunner runner;

    public JavaToolkit(ProcessRunner runner) { this.runner = runner; }

    /** Chemin de l'exécutable {@code java} de la JVM courante. */
    public String javaExe() {
        return Path.of(System.getProperty("java.home"), "bin", "java").toString();
    }

    /** Chemin de l'exécutable {@code javac} de la JVM courante. */
    public String javacExe() {
        return Path.of(System.getProperty("java.home"), "bin", "javac").toString();
    }

    /**
     * Classpath d'outillage : l'uber-jar courant s'il s'agit d'un .jar (JUnit/AssertJ/Checkstyle shadés),
     * sinon {@code java.class.path} (dev / surefire).
     */
    public String toolingClasspath() {
        try {
            var loc = JavaToolkit.class.getProtectionDomain().getCodeSource().getLocation();
            Path p = Path.of(loc.toURI());
            if (p.toString().endsWith(".jar")) return p.toString();
        } catch (Exception ignore) {
            // retombe sur java.class.path
        }
        return System.getProperty("java.class.path");
    }

    /** {@code javac -d outDir -cp classpath <*.java sous srcDirs>}. */
    public ProcessResult compile(List<Path> srcDirs, Path outDir, String classpath) throws IOException {
        Files.createDirectories(outDir);
        List<String> files = javaFiles(srcDirs);
        if (files.isEmpty()) {
            return new ProcessResult(2, "", "aucun fichier .java à compiler dans " + srcDirs);
        }
        List<String> cmd = new ArrayList<>();
        cmd.add(javacExe());
        cmd.add("-d"); cmd.add(outDir.toString());
        if (classpath != null && !classpath.isBlank()) { cmd.add("-cp"); cmd.add(classpath); }
        cmd.addAll(files);
        return runner.run(outDir, cmd);
    }

    /**
     * Lance JUnit via le ConsoleLauncher.
     * {@code java -cp classpath org.junit.platform.console.ConsoleLauncher execute
     *        --disable-banner --details=summary --fail-if-no-tests (--select-class=<fqcn>)+}
     */
    public ProcessResult runJUnit(Path workDir, String classpath, List<String> selectClasses) throws IOException {
        List<String> cmd = new ArrayList<>();
        cmd.add(javaExe());
        cmd.add("-cp"); cmd.add(classpath);
        cmd.add("org.junit.platform.console.ConsoleLauncher");
        cmd.add("execute");
        cmd.add("--disable-banner");
        cmd.add("--details=summary");
        cmd.add("--fail-if-no-tests");
        for (String fqcn : selectClasses) cmd.add("--select-class=" + fqcn);
        return runner.run(workDir, cmd);
    }

    /** {@code java -cp <toolingClasspath> com.puppycrawl.tools.checkstyle.Main -c config <fichiers>}. */
    public ProcessResult checkstyle(Path workDir, Path config, List<Path> srcDirs) throws IOException {
        List<String> files = javaFiles(srcDirs);
        List<String> cmd = new ArrayList<>();
        cmd.add(javaExe());
        cmd.add("-cp"); cmd.add(toolingClasspath());
        cmd.add("com.puppycrawl.tools.checkstyle.Main");
        cmd.add("-c"); cmd.add(config.toString());
        cmd.addAll(files);
        return runner.run(workDir, cmd);
    }

    static List<String> javaFiles(List<Path> srcDirs) {
        List<String> out = new ArrayList<>();
        for (Path dir : srcDirs) {
            if (!Files.isDirectory(dir)) continue;
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.filter(p -> p.getFileName().toString().endsWith(".java"))
                    .map(Path::toString).sorted().forEach(out::add);
            } catch (IOException e) {
                throw new UncheckedIOException("scan .java échoué : " + dir, e);
            }
        }
        return out;
    }
}
