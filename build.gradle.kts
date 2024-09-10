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
    implementation("cz.lukynka:lkws:1.2")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")
    implementation("software.amazon.awssdk:s3:2.27.22")
    implementation("com.akuleshov7:ktoml-core:0.5.1")
    implementation("com.akuleshov7:ktoml-file:0.5.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    jvmToolchain(21)
}