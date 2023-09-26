package com.wordletime.routing

import com.wordletime.config.ServerConfig
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Routing
import io.swagger.v3.oas.models.servers.Server
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.apiRouting() {
  swaggerOpenAPI()
  guessRouting()
  requirementsRouting()
}

private fun Routing.swaggerOpenAPI() {
  val serverConfig: ServerConfig by closestDI().instance()
  openAPI("openapi", swaggerFile = "wordle_time-openapi.yaml") {
    this.opts.openAPI.apply {
      this.servers = listOf(Server().apply { url = "https://${serverConfig.host}:${serverConfig.port}" })
    }
  }
  swaggerUI(path = "swagger", swaggerFile = "wordle_time-openapi.yaml")
}

