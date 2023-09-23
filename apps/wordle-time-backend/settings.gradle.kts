pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "wordle-time-backend"

fun VersionCatalogBuilder.addCommon(alias: String, commonGroup: String, version: String, artifactList: List<String>) {
  val versionRef = version(alias, version)
  artifactList.forEach { artifact ->
    library(artifact, commonGroup, artifact).versionRef(versionRef)
  }

  if (artifactList.size > 1)
    bundle(versionRef, artifactList)
}

dependencyResolutionManagement {
  versionCatalogs {
    val coroutineVersion = "1.7.3"
    val ktorVersion = "2.3.4"

    create("libs") {
      //Server - ktor
      addCommon(
        "ktor", "io.ktor", ktorVersion, listOf(
          "ktor-server-core",
          "ktor-server-netty",
          "ktor-server-call-logging",
          "ktor-server-config-yaml",
          "ktor-server-content-negotiation",
          "ktor-serialization-kotlinx-json",
          "ktor-server-resources",
          "ktor-server-request-validation",
          "ktor-server-openapi",
          "ktor-server-swagger",
          "ktor-server-cors"
        )
      )

      //Swagger Codegen
      val swagger = version("swagger", "1.0.42")
      library(swagger, "io.swagger.codegen.v3", "swagger-codegen-generators").versionRef(swagger)

      //DI - Kodein
      addCommon(
        "kodein", "org.kodein.di", "7.20.2", listOf(
          "kodein-di", "kodein-di-framework-ktor-server-jvm"
        )
      )

      //Config - hoplite
      addCommon(
        "hoplite", "com.sksamuel.hoplite", "2.7.5", listOf(
          "hoplite-core", "hoplite-yaml", "hoplite-json"
        )
      )

      //Logging - log4j2
      addCommon(
        "log4j2", "org.apache.logging.log4j", "2.20.0", listOf(
          "log4j", "log4j-api", "log4j-core", "log4j-slf4j2-impl"
        )
      )
      val jackson = version("jackson", "2.15.2")
      library(
        "jacksonDataformatYaml",
        "com.fasterxml.jackson.dataformat",
        "jackson-dataformat-yaml"
      ).versionRef(jackson)
      library(
        "jacksonCore",
        "com.fasterxml.jackson.core",
        "jackson-databind"
      ).versionRef(jackson)

      //Logging - kotlin-logging
      val kotlinLogging = version("kotlinLogging", "5.1.0")
      library(kotlinLogging, "io.github.oshai", "kotlin-logging").versionRef(kotlinLogging)

      //Parallel Processing - coroutines
      val coroutines = version("coroutines", coroutineVersion)
      library(coroutines, "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(coroutines)
    }

    create("testLibs") {
      // Testing - kotlin-test-junit5
      addCommon(
        "junit", "org.junit.jupiter", "5.10.0", listOf(
          "junit-jupiter-api", "junit-jupiter-engine", "junit-jupiter-params"
        )
      )

      // Testing - JUnit Platform launcher
      val junitPlatformLauncher = version("junitPlatformLauncher", "1.10.0")
      library(junitPlatformLauncher, "org.junit.platform", "junit-platform-launcher").versionRef(junitPlatformLauncher)

      // Testing - ktor-host
      addCommon(
        "ktor", "io.ktor", ktorVersion, listOf(
          "ktor-server-test-host", "ktor-client-content-negotiation", "ktor-client-resources"
        )
      )

      //Mocking - mockk
      val mockk = version("mockk", "1.13.5")
      library("mockk", "io.mockk", "mockk").versionRef(mockk)

      //Parallel Processing - coroutines
      val coroutines = version("coroutines", coroutineVersion)
      library(coroutines, "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef(coroutines)
    }
  }
}
