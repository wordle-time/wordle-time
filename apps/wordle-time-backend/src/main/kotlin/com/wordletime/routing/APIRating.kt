package com.wordletime.routing

import com.wordletime.config.ServerConfig
import io.ktor.server.application.Application
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import io.swagger.v3.oas.models.servers.Server

fun Application.apiRouting(serverConfig: ServerConfig) {
  routing {
    swaggerOpenAPI(serverConfig)
    guessRouting()
    requirementsRouting()
  }
}

private fun Routing.swaggerOpenAPI(serverConfig: ServerConfig) {
  openAPI("openapi", swaggerFile = "wordle_time-openapi.yaml") {
    this.opts.openAPI.apply {
      this.servers = listOf(Server().apply { url = "https://${serverConfig.host}:${serverConfig.port}" })
    }
  }
  swaggerUI(path = "swagger", swaggerFile = "wordle_time-openapi.yaml")
}
