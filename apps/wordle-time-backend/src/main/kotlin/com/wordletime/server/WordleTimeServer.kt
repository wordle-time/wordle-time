package com.wordletime.server

import com.wordletime.config.Config
import com.wordletime.di.setupDI
import com.wordletime.routing.apiRouting
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStarting
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.ServerReady
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

class WordleTimeServer(private val config: Config, private val serverScope: CoroutineScope) {
  enum class ServerState {
    ApplicationStarting,
    ApplicationStarted,
    ServerReady,
    ApplicationStopPreparing,
    ApplicationStopping,
    ApplicationStopped
  }

  private val serverConfig by config::server

  private val internalServerState = MutableSharedFlow<ServerState>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )


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

  val serverState = internalServerState.asSharedFlow().also {
    it.onEach { newState ->
      logger.info { newState.name }
      if (newState == ServerState.ApplicationStopped) {
        serverScope.cancel("Server stopped")
      }
    }.launchIn(serverScope)
  }

  fun startServer() = serverScope.launch {
    serverEngine.start(false)
    awaitCancellation()
  }

  private fun Application.wordleTimeServer(config: Config) {
    setupDI(config)
    installPlugins()
    setupLifecycle()
    apiRouting()
  }


  private fun Application.setupLifecycle() {
    listOf(
      ApplicationStarting to ServerState.ApplicationStarting,
      ApplicationStarted to ServerState.ApplicationStarted,
      ApplicationStopping to ServerState.ApplicationStopping,
      ApplicationStopped to ServerState.ApplicationStopped
    ).forEach { (ktorEvent, enumEvent) ->
      environment.monitor.subscribe(ktorEvent) { _ ->
        internalServerState.tryEmit(enumEvent)
      }
    }

    listOf(
      ServerReady to ServerState.ServerReady,
      ApplicationStopPreparing to ServerState.ApplicationStopPreparing,
    ).forEach { (ktorEvent, enumEvent) ->
      environment.monitor.subscribe(ktorEvent) { _ ->
        internalServerState.tryEmit(enumEvent)
      }
    }
  }
}
