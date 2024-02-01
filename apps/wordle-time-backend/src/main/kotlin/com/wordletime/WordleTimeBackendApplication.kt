package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.server.WordleTimeServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  val config = ConfigLoaderBuilder.default()
    .addResourceSource("/application.yml")
    .build()
    .loadConfigOrThrow<Config>()

  WordleTimeServer(config, CoroutineScope(Dispatchers.Default))
    .startServer().join()
}
