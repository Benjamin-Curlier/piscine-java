plugins {
    application
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":console"))
    implementation(project(":framework"))
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass = "etnc.piscine.moulinette.gui.Main"
}

tasks.shadowJar {
    archiveFileName = "moulinette-gui.jar"
    // Mêmes règles que l'uber-jar console : l'uber-jar EST le classpath d'outillage
    // (JavaToolkit), et les signatures des dépendances shadées doivent être exclues.
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    mergeServiceFiles()
}
