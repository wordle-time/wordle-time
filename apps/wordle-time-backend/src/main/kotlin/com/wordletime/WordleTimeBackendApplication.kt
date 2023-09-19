package com.wordletime

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.di.serverModule
import com.wordletime.routing.apiRouting
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
import io.ktor.server.plugins.callloging.processingTimeMillis
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.resources.Resources
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.di
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun main() {
  val config = ConfigLoaderBuilder.default()
    .addResourceSource("/application.yml")
    .build()
    .loadConfigOrThrow<Config>()

  val serverConfig by config::server

  embeddedServer(
    Netty,
    host = serverConfig.host,
    port = serverConfig.port,
    module = { wordleTimeServer(config) }
  ).also {
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
      it.stop(1, 5, TimeUnit.SECONDS)
    })
  }.start(wait = true)
}

private fun Application.wordleTimeServer(config: Config) {
  setupDI(config)
  installPlugins()
  setupLifecycle()
  apiRouting()
}

private fun Application.setupDI(config: Config) {
  di {
    import(serverModule(config))
  }
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
    format {
      val status = it.response.status()
      val method = it.request.httpMethod.value
      val route = it.request.path()
      val time = it.processingTimeMillis()

      buildString {
        append("$status: $method - $route in ${time}ms")
        it.request.queryParameters.forEach { prameter, values ->
          append(
            "\n\tQueryParameter: $prameter -> " +
              values.joinToString(", ", "[", "]") { value -> "\"$value\"" }
          )
        }
        it.request.cookies["gameID"]?.let { gameID ->
          append("\n\tCookie: gameID -> $gameID")
        }
      }


    }
  }
  install(CORS) {
    val serverConfig by this@installPlugins.closestDI().instance<ServerConfig>()
    allowHost("${serverConfig.host}:${serverConfig.frontendPort}")
    allowMethod(HttpMethod.Get)

    allowHeader(HttpHeaders.ContentType)
  }
}

private fun Application.setupLifecycle() {
  environment.monitor.subscribe(ApplicationStopping) {
    println("Gracefully shutting down")
  }
}

