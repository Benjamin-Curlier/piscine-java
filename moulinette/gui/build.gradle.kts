plugins {
    application
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
