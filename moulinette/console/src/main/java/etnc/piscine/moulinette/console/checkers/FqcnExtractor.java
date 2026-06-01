package etnc.piscine.moulinette.console.checkers;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** Reconstruit le nom pleinement qualifié d'une classe Java à partir de son fichier source. */
public final class FqcnExtractor {

    private static final Pattern PACKAGE = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;");

    private FqcnExtractor() {}

    public static String fqcn(Path javaFile) {
        String fileName = javaFile.getFileName().toString();
        String className = fileName.endsWith(".java")
            ? fileName.substring(0, fileName.length() - ".java".length())
            : fileName;
        try {
            for (String line : Files.readAllLines(javaFile)) {
                Matcher m = PACKAGE.matcher(line);
                if (m.find()) return m.group(1) + "." + className;
            }
            return className;
        } catch (IOException e) {
            throw new UncheckedIOException("lecture FQCN échouée : " + javaFile, e);
        }
    }

    /** Tous les FQCN des fichiers .java sous un répertoire (récursif). Vide si le répertoire n'existe pas. */
    public static List<String> fqcnsUnder(Path root) {
        if (!Files.isDirectory(root)) return List.of();
        try (Stream<Path> walk = Files.walk(root)) {
            return walk.filter(p -> p.getFileName().toString().endsWith(".java"))
                       .map(FqcnExtractor::fqcn)
                       .sorted()
                       .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("scan FQCN échoué : " + root, e);
        }
    }
}
