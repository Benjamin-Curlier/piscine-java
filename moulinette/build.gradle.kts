plugins {
    java
}

subprojects {
    apply(plugin = "java")

    group = "piscine.moulinette"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        // Sortie lisible comme surefire
        testLogging {
            events("failed", "skipped")
            showStandardStreams = false
        }
    }
}
