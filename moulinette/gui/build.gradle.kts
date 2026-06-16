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
    mainClass = "piscine.moulinette.gui.Main"
}

tasks.shadowJar {
    archiveFileName = "moulinette-gui.jar"
    // Mêmes règles que l'uber-jar console : l'uber-jar EST le classpath d'outillage
    // (JavaToolkit), et les signatures des dépendances shadées doivent être exclues.
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    mergeServiceFiles()
}

// ── Installeur jpackage (chantier 6 v1) ──────────────────────────────────────
// Staging : uber-jar + contenu piscine (exercises, site de cours construit) + MinGit (Windows).
// Le `Main` zéro-argument retrouve `piscine/` et `git/` à côté du jar dans l'app installée.

val appVersion = "0.7.0" // jpackage Windows exige x.y.z numérique
val mingitVersion = "2.54.0"
val mingitUrl = "https://github.com/git-for-windows/git/releases/download/" +
    "v$mingitVersion.windows.1/MinGit-$mingitVersion-64-bit.zip"
val isWindows = System.getProperty("os.name").lowercase().contains("win")

val downloadMinGit = tasks.register("downloadMinGit") {
    val zip = layout.buildDirectory.file("mingit/MinGit-$mingitVersion.zip")
    outputs.file(zip)
    onlyIf { isWindows }
    doLast {
        val target = zip.get().asFile
        if (!target.exists()) {
            target.parentFile.mkdirs()
            ant.invokeMethod("get", mapOf("src" to mingitUrl, "dest" to target))
        }
    }
}

val jpackageStaging = tasks.register<Copy>("jpackageStaging") {
    dependsOn(tasks.shadowJar, downloadMinGit)
    val staging = layout.buildDirectory.dir("jpackage/staging")
    into(staging)
    from(tasks.shadowJar.flatMap { it.archiveFile })
    // target/ : artefacts Maven locaux (non versionnés, chemins > MAX_PATH qui cassent WiX)
    from(rootProject.projectDir.parentFile.resolve("exercises")) {
        into("piscine/exercises")
        exclude("**/target/**", "**/.piscine/**")
    }
    val site = rootProject.projectDir.parentFile.resolve("courses/build")
    if (site.isDirectory) {
        from(site) { into("piscine/site") }
    } else {
        logger.lifecycle("[jpackage] courses/build absent — installeur sans site de cours (npm run build pour l'inclure)")
    }
    if (isWindows) {
        from(zipTree(layout.buildDirectory.file("mingit/MinGit-$mingitVersion.zip"))) { into("git") }
    }
}

// Type d'installeur : exe (Windows, WiX requis), deb (Linux), ou app-image (portable, défaut hors Windows).
val jpackageType = providers.gradleProperty("jpackageType")
    .getOrElse(if (isWindows) "exe" else "app-image")

tasks.register<Exec>("jpackageApp") {
    dependsOn(jpackageStaging)
    val outDir = layout.buildDirectory.dir("jpackage/out")
    // pas de purge du dossier de sortie : le CI release y dépose plusieurs types (deb puis app-image)
    doFirst { outDir.get().asFile.mkdirs() }
    val javaHome = javaToolchains.launcherFor(java.toolchain).get().metadata.installationPath
    val args = mutableListOf(
        javaHome.file("bin/jpackage").asFile.absolutePath,
        "--type", jpackageType,
        "--name", "Piscine Java",
        "--app-version", appVersion,
        "--vendor", "Piscine Java",
        "--input", layout.buildDirectory.dir("jpackage/staging").get().asFile.absolutePath,
        "--main-jar", "moulinette-gui.jar",
        "--dest", outDir.get().asFile.absolutePath,
    )
    if (jpackageType == "exe") {
        args += listOf("--win-per-user-install", "--win-dir-chooser", "--win-menu", "--win-shortcut")
    }
    if (jpackageType == "deb") {
        args += listOf("--linux-shortcut")
    }
    // WiX (requis pour --type exe) : dossier portable via -PwixDir=... ou env WIX_DIR, ajouté au PATH
    val wixDir = providers.gradleProperty("wixDir").orNull ?: System.getenv("WIX_DIR")
    if (wixDir != null) {
        environment("PATH", wixDir + File.pathSeparator + System.getenv("PATH"))
    }
    commandLine(args)
}
