package com.wordletime.server

import com.wordletime.config.Config
import com.wordletime.di.setupDI
import com.wordletime.routing.apiRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class WordleTimeServer(private val config: Config) {
  private val serverConfig by config::server

  private val serverEngine = embeddedServer(
    Netty,
    host = serverConfig.host,
    port = serverConfig.port,
    module = { wordleTimeServer(config) }
  ).also {
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
      it.stop(1, 5, TimeUnit.SECONDS)
    })
  }

  fun startServer() = serverEngine.start(true)

  private fun Application.wordleTimeServer(config: Config) {
    setupDI(config)
    installPlugins()
    setupLifecycle()
    apiRouting()
  }
}
