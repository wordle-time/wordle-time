package com.wordletime.server

import com.wordletime.config.ServerConfig
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.callloging.processingTimeMillis
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.resources.Resources
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.installPlugins() {
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
