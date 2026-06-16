package piscine.moulinette.console.workspace;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExerciseCatalogTest {

    @Test
    void scanne_les_exos_et_les_groupe_tries_par_position(@TempDir Path piscine) throws IOException {
        writeExo(piscine, "module-1-fondamentaux/1.1.1-hello-world", """
            slug: hello-world
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: [syntaxe-de-base]
            """);
        writeExo(piscine, "module-1-fondamentaux/1.1.2-affichage-formate", """
            slug: affichage-formate
            module: 1
            sous_groupe: "1.1"
            position: 2
            notions: [printf]
            """);
        writeExo(piscine, "module-1-fondamentaux/1.2.1-conversion-unites", """
            slug: conversion-unites
            module: 1
            sous_groupe: "1.2"
            position: 1
            notions: [variables]
            """);

        ExerciseCatalog cat = ExerciseCatalog.scan(piscine.resolve("exercises"));

        assertThat(cat.sousGroupes()).extracting(SousGroupe::id).containsExactly("1.1", "1.2");
        assertThat(cat.sousGroupe("1.1").exercices())
            .extracting(ExerciseEntry::id).containsExactly("1.1.1", "1.1.2");
    }

    @Test
    void exception_si_sous_groupe_a_plus_de_6_exos(@TempDir Path piscine) throws IOException {
        for (int i = 1; i <= 7; i++) {
            writeExo(piscine, "module-1-fondamentaux/1.1." + i + "-exo" + i, """
                slug: exo%d
                module: 1
                sous_groupe: "1.1"
                position: %d
                notions: []
                """.formatted(i, i));
        }
        assertThatThrownBy(() -> ExerciseCatalog.scan(piscine.resolve("exercises")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("plus de 6");
    }

    @Test
    void metadata_yml_invalide_est_loggee_et_skippee(@TempDir Path piscine) throws IOException {
        writeExo(piscine, "module-1-fondamentaux/1.1.1-ok", """
            slug: ok
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        Path bad = piscine.resolve("exercises/module-1-fondamentaux/1.1.2-bad");
        Files.createDirectories(bad);
        Files.writeString(bad.resolve("metadata.yml"), "::: not yaml :::");

        ExerciseCatalog cat = ExerciseCatalog.scan(piscine.resolve("exercises"));
        assertThat(cat.sousGroupe("1.1").exercices()).hasSize(1);
    }

    @Test
    void projet_binome_est_ignore_proprement_du_catalogue(@TempDir Path piscine) throws IOException {
        writeExo(piscine, "module-1-fondamentaux/1.1.1-ok", """
            slug: ok
            module: 1
            sous_groupe: "1.1"
            position: 1
            notions: []
            """);
        // Schéma binôme : binome: true, duree_estimee_h, pas de position ni de sous_groupe.
        // Ces projets sont évalués par le formateur, pas notés par la console : ils ne doivent
        // pas figurer au catalogue, et ne doivent PAS provoquer de warning « metadata invalide ».
        Path binome = piscine.resolve("exercises/projets-binome/projet-1-mini-domaine");
        Files.createDirectories(binome);
        Files.writeString(binome.resolve("metadata.yml"), """
            slug: projet-1
            titre: "Projet binôme #1"
            binome: true
            module: 3
            duree_estimee_h: 14
            notions: []
            """);

        ExerciseCatalog cat = ExerciseCatalog.scan(piscine.resolve("exercises"));

        assertThat(cat.sousGroupes()).extracting(SousGroupe::id).containsExactly("1.1");
    }

    private static void writeExo(Path piscine, String relPath, String metadata) throws IOException {
        Path dir = piscine.resolve("exercises").resolve(relPath);
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("metadata.yml"), metadata);
        Files.createDirectories(dir.resolve("starter"));
    }
}
