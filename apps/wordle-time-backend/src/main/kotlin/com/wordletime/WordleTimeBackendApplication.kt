package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.server.WordleTimeServer
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val logger = KotlinLogging.logger {}
  logger.error { "This is an error" }
  logger.warn { "This is a warning" }
  logger.info { "This is an info" }
  logger.trace { "This is a trace" }
  Level.entries.forEach {
    println("Level ${it.name}: ${logger.isLoggingEnabledFor(it)}")
  }

  System.out.println("This is out output")
  System.err.println("This is err output")

  val config = ConfigLoaderBuilder.default()
    .addResourceSource("/application.yml")
    .build()
    .loadConfigOrThrow<Config>()

  WordleTimeServer(config, CoroutineScope(Dispatchers.Default))
    .startServer().join()
}
