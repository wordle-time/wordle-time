pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

/*
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
 */

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

    create("libs") {
      //Server - ktor
      addCommon(
        "ktor", "io.ktor", "2.3.3", listOf(
          "ktor-server-core",
          "ktor-server-netty",
          "ktor-server-call-logging",
          "ktor-server-config-yaml",
          "ktor-server-content-negotiation",
          "ktor-serialization-kotlinx-json",
          "ktor-server-resources",
          "ktor-server-request-validation",
          "ktor-server-openapi",
          "ktor-server-swagger"
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
      addCommon("hoplite", "com.sksamuel.hoplite", "2.7.5", listOf(
        "hoplite-core", "hoplite-yaml", "hoplite-json"
      ))

      //Logging - slf4j-simple
      val slf4jSimple = version("slf4jSimple", "2.0.7")
      library(slf4jSimple, "org.slf4j", "slf4j-simple").versionRef(slf4jSimple)

      //Logging - kotlin-logging
      val kotlinLogging = version("kotlinLogging", "5.1.0")
      library(kotlinLogging, "io.github.oshai", "kotlin-logging").versionRef(kotlinLogging)

      //Parallel Processing - coroutines
      val coroutines = version("coroutines", coroutineVersion)
      library(coroutines, "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(coroutines)
    }

    create("testLibs") {
      // Testing - kotlin-test-junit5
      val kotlinTestJunit5 = version("kotlin-test-junit5", "1.9.0")
      library("kotlin-test-junit5", "org.jetbrains.kotlin", "kotlin-test-junit5").versionRef(kotlinTestJunit5)

      //Mocking - mockk
      val mockk = version("mockk", "1.13.5")
      library("mockk", "io.mockk", "mockk").versionRef(mockk)

      //Parallel Processing - coroutines
      val coroutines = version("coroutines", coroutineVersion)
      library(coroutines, "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef(coroutines)
    }
  }
}
