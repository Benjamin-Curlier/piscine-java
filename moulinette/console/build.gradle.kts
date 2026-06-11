plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":runner"))
    implementation(project(":reports"))
    implementation(libs.slf4j.api)
    implementation(libs.snakeyaml)
    runtimeOnly(libs.logback.classic)

    // Outillage d'évaluation : shadé dans l'uber-jar, invoqué en sous-processus
    // par les Checkers (l'uber-jar EST le classpath). Présent aussi au classpath
    // de test via testRuntimeClasspath (fallback java.class.path de JavaToolkit).
    runtimeOnly(libs.junit.jupiter)
    runtimeOnly(libs.junit.platform.console)
    runtimeOnly(libs.assertj.core)
    runtimeOnly(libs.checkstyle)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass = "etnc.piscine.moulinette.console.Main"
}

// Suite par défaut : exclut les tags lourds (équivalent surefire.excludedGroups=git,e2e,tools)
tasks.test {
    useJUnitPlatform {
        excludeTags("git", "e2e", "tools")
    }
}

tasks.shadowJar {
    archiveFileName = "moulinette-console.jar"
    // Exclut les fichiers de signature des dépendances shadées (checkstyle & transitives
    // sont signées) : sinon le manifeste fusionné déclenche "Invalid signature file digest".
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    mergeServiceFiles()
}

// Suites lourdes à la demande (équivalent mvn -Dgroups=git|tools|e2e)
listOf("git" to "testGit", "tools" to "testTools", "e2e" to "testE2e").forEach { (tag, taskName) ->
    tasks.register<Test>(taskName) {
        description = "Suite taguée '$tag'"
        group = "verification"
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath
        useJUnitPlatform { includeTags(tag) }
    }
}
