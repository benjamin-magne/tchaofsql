plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation(project(":utils"))
    implementation("com.github.ajalt.clikt:clikt:5.0.1")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    testImplementation(kotlin("test"))
    implementation("org.postgresql:postgresql:42.7.14")
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "net.unotec.app.AppKt"
}

tasks.test {
    useJUnitPlatform()
}
