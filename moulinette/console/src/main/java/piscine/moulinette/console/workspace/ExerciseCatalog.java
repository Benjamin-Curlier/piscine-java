package piscine.moulinette.console.workspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class ExerciseCatalog {
    private static final Logger LOG = LoggerFactory.getLogger(ExerciseCatalog.class);

    private final Map<String, SousGroupe> bySousGroupe;

    private ExerciseCatalog(Map<String, SousGroupe> bySousGroupe) {
        this.bySousGroupe = bySousGroupe;
    }

    public List<SousGroupe> sousGroupes() {
        return bySousGroupe.values().stream()
            .sorted(Comparator.comparing(SousGroupe::id))
            .toList();
    }

    public SousGroupe sousGroupe(String id) {
        SousGroupe sg = bySousGroupe.get(id);
        if (sg == null) throw new NoSuchElementException("sous-groupe inconnu : " + id);
        return sg;
    }

    public static ExerciseCatalog scan(Path exercisesRoot) {
        if (!Files.isDirectory(exercisesRoot)) {
            throw new IllegalArgumentException("exercises/ introuvable : " + exercisesRoot);
        }
        Map<String, List<ExerciseEntry>> byGroup = new LinkedHashMap<>();
        try (Stream<Path> walk = Files.walk(exercisesRoot, 3)) {
            walk.filter(p -> p.getFileName().toString().equals("metadata.yml"))
                .forEach(meta -> parseOne(meta).ifPresent(e ->
                    byGroup.computeIfAbsent(e.sousGroupe(), k -> new ArrayList<>()).add(e)));
        } catch (IOException ioe) {
            throw new IllegalStateException("scan exercises échoué", ioe);
        }
        Map<String, SousGroupe> result = new TreeMap<>();
        for (var e : byGroup.entrySet()) {
            List<ExerciseEntry> sorted = e.getValue().stream()
                .sorted(Comparator.comparingInt(ExerciseEntry::position))
                .toList();
            result.put(e.getKey(), new SousGroupe(e.getKey(), "", sorted));
        }
        return new ExerciseCatalog(result);
    }

    @SuppressWarnings("unchecked")
    private static Optional<ExerciseEntry> parseOne(Path metadataYml) {
        try {
            Map<String, Object> m;
            try (var reader = Files.newBufferedReader(metadataYml)) {
                m = new Yaml().load(reader);
            }
            if (m == null) throw new IOException("vide");
            // Les projets binôme (schéma distinct : binome:true, duree_estimee_h, sans position)
            // sont évalués par le formateur, pas notés par la console : on les ignore proprement
            // du catalogue (sans warning « metadata invalide », qui polluerait la console stagiaire).
            if (Boolean.TRUE.equals(m.get("binome"))) {
                LOG.debug("projet binôme ignoré du catalogue console (évalué par le formateur) : {}", metadataYml);
                return Optional.empty();
            }
            Path exoDir = metadataYml.getParent();
            String slug = String.valueOf(m.get("slug"));
            int module = ((Number) m.get("module")).intValue();
            String sousGroupe = String.valueOf(m.get("sous_groupe"));
            int position = ((Number) m.get("position")).intValue();
            List<String> notions = (List<String>) m.getOrDefault("notions", List.of());
            String id = exoDir.getFileName().toString().split("-")[0]; // "1.1.1"
            return Optional.of(new ExerciseEntry(id, slug, module, sousGroupe, position, notions, exoDir));
        } catch (Exception e) {
            LOG.warn("metadata.yml invalide ignoré : {} ({})", metadataYml, e.getMessage());
            return Optional.empty();
        }
    }
}
