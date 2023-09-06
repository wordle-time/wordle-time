plugins {
    kotlin("jvm") version "1.9.0"
    application
  kotlin("plugin.serialization") version "1.9.0"
}

group = "com.wordletime"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
  implementation(libs.bundles.ktor) {
    exclude("ch.qos.logback", "logback-classic")
    exclude("ch.qos.logback", "logback-core")
  }
  implementation(libs.swagger) {
    exclude("ch.qos.logback", "logback-classic")
    exclude("ch.qos.logback", "logback-core")
  }
  implementation(libs.bundles.kodein)
  implementation(libs.bundles.hoplite)
  implementation(libs.slf4jSimple)
  implementation(libs.kotlinLogging)
  implementation(libs.coroutines)

  testImplementation(testLibs.kotlin.test.junit5)
  testImplementation(testLibs.mockk)
  testImplementation(testLibs.coroutines)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
