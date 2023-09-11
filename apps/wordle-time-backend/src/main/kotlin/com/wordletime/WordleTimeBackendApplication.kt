package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordProviderConfig
import com.wordletime.requirements.RequirementsProvider
import com.wordletime.routing.apiRouting
import com.wordletime.wordProvider.ListWordProvider
import com.wordletime.wordProvider.StaticWordProvider
import com.wordletime.wordProvider.WordProvider
import com.wordletime.wordProvider.WordState
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
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
  bind<WordProviderConfig> { singleton { instance<Config>().wordProviderConfig } }
}

fun main() {
  val serverConfig by confDI.instance<ServerConfig>()
  embeddedServer(
    Netty,
    host = serverConfig.host,
    port = serverConfig.port,
    module = Application::wordleTimeServer
  ).also {
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
      it.stop(1, 5, TimeUnit.SECONDS)
    })
  }.start(wait = true)
}

private fun Application.wordleTimeServer() {
  installPlugins()
  setupDI()
  setupLifecycle()

  val serverConfig by confDI.instance<ServerConfig>()
  apiRouting(serverConfig)
}

private fun Application.installPlugins() {
  install(Resources)
  install(ContentNegotiation) {
    json()
  }
  /*
  install(RequestValidation) {
  }
   */
  install(CallLogging) {
    level = Level.TRACE
  }
  install(CORS) {
    val serverConfig by confDI.instance<ServerConfig>()
    allowHost("${serverConfig.host}:${serverConfig.frontendPort}")
    allowMethod(HttpMethod.Get)

    allowHeader(HttpHeaders.ContentType)
  }
}

fun Application.setupDI() {
  di {
    extend(confDI)
    bind<WordProvider> {
      singleton {
        val wordProviderConfig: WordProviderConfig by di.instance()
        wordProviderConfig.staticWord.let { provideWord ->
          check(provideWord.isEmpty() || provideWord.length == 5) {
            "The configured wordl Word must either be empty to randomly choose a word from words.json or 5 letters long"
          }
          if (provideWord.isEmpty()) ListWordProvider() else StaticWordProvider(provideWord)
        }
      }
    }
    bind<WordState> { singleton { WordState(instance<WordProvider>()) } }
    bind<RequirementsProvider> { singleton { RequirementsProvider() } }
  }
}

private fun Application.setupLifecycle() {
  environment.monitor.subscribe(ApplicationStopping) {
    println("Gracefully shutting down")
  }
}
