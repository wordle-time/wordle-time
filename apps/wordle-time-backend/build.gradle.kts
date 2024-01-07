import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  kotlin("jvm") version "1.9.22"
  application
  kotlin("plugin.serialization") version "1.9.22"
  idea
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

  implementation(libs.bundles.log4j2)
  implementation(libs.jacksonDataformatYaml)
  implementation(libs.jacksonCore)

  implementation(libs.kotlinLogging)
  implementation(libs.coroutines)

  testImplementation(testLibs.bundles.junit)
  testImplementation(testLibs.junitPlatformLauncher)
  testImplementation(testLibs.bundles.ktor)
  testImplementation(testLibs.mockk)
  testImplementation(testLibs.coroutines)
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events = setOf(
      TestLogEvent.PASSED,
      TestLogEvent.SKIPPED,
      TestLogEvent.FAILED,
      TestLogEvent.STANDARD_OUT,
      TestLogEvent.STANDARD_ERROR
    )
  }
}


kotlin {
  jvmToolchain(21)
}

tasks.named<JavaExec>("run") {
  System.getProperties().filter {
    val keyAsString = it.key as String
    keyAsString.startsWith("config.override")
  }.forEach {
    systemProperties[it.key as String] = it.value as String
  }
}

application {
  mainClass.set("com.wordletime.WordleTimeBackendApplicationKt")
}

tasks.processResources {
  from("../../docs/.requirements") {
    into("requirements/")
    exclude("**/*.md")
    exclude("**/*.pdf")
  }
  from("../../docs/other") {
    into("other")
    exclude("**/*.md")
    exclude("**/*.pdf")
  }
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}
