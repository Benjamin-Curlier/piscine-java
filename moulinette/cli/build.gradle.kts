plugins {
    application
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":runner"))
    implementation(project(":reports"))
    implementation(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.logback.classic)
}

application {
    mainClass = "etnc.piscine.moulinette.cli.Main"
}
