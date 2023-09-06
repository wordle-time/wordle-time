package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.routing.setupAPIRouting
import com.wordletime.wordProvider.StaticWordProvider
import com.wordletime.wordProvider.WordProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.resources.Resources
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

private val confDI = DI {
  bind<Config> {
    singleton {
      ConfigLoaderBuilder.default()
        .addResourceSource("/application.yml")
        .build()
        .loadConfigOrThrow<Config>()
    }
  }
  bind<ServerConfig> { singleton { instance<Config>().server } }
}

fun main() {
  val serverConfig by confDI.instance<ServerConfig>()
  embeddedServer(
    Netty,
    port = serverConfig.port,
    module = Application::partyTimeServer,
  ).also {
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
      it.stop(1, 5, TimeUnit.SECONDS)
    })
  }.start(wait = true)
}

private fun Application.partyTimeServer() {
  installPlugins()
  setupDI()
  setupLifecycle()
  setupAPIRouting()
}

private fun Application.installPlugins() {
  install(Resources)
  install(ContentNegotiation) {
    json()
  }
  install(RequestValidation) {
    /*
    validate<AccountDeleteDTO> {
      ValidationResult.Valid
    }
     */
  }
  install(CallLogging) {
    level = Level.TRACE
  }
}

fun Application.setupDI() {
  di {
    extend(confDI)
    bind<WordProvider> { singleton { StaticWordProvider() } }
  }
}

private fun Application.setupLifecycle() {
  environment.monitor.subscribe(ApplicationStopping) {
    println("Gracefully shutting down")
  }
}
