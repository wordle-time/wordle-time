package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.server.WordleTimeServer

fun main() {
  val config = ConfigLoaderBuilder.default()
    .addResourceSource("/application.yml")
    .build()
    .loadConfigOrThrow<Config>()

  WordleTimeServer(config).startServer()
}
