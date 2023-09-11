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
  constraints {
    implementation("commons-codec", "commons-codec", "1.16.0") {
      because("version 1.11 - 1.15 have junit security vulnerability")
    }
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

tasks.named<JavaExec>("run") {
  systemProperties["config.override.wordProviderConfig.staticWord"] =
    System.getProperty("config.override.wordProviderConfig.staticWord")
}

application {
  mainClass.set("com.wordletime.WordleTimeBackendApplicationKt")
}

tasks.processResources {
  from("../../docs/requirements") {
    into("requirements/")
    exclude("**/*.md")
    exclude("**/*.pdf")
  }
}
