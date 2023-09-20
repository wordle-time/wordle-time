package com.wordletime.server

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping

fun Application.setupLifecycle() {
  environment.monitor.subscribe(ApplicationStopping) {
    println("Gracefully shutting down")
  }
}
