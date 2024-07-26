plugins {
    kotlin("plugin.serialization") version "1.9.22"
    kotlin("jvm") version "1.9.22"
}

group = "cz.lukynka"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "devOS"
        url = uri("https://mvn.devos.one/releases")
    }
}

dependencies {
    implementation("cz.lukynka:pretty-log:1.4")
    implementation("cz.lukynka:lkws:1.1")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(21)
}