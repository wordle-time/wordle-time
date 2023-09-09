package com.wordletime.routing

import com.wordletime.requirements.RequirementsProvider
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.requirementsRouting() {
  get<API.Requirements> {
    val requirementsProvider: RequirementsProvider by closestDI().instance()
    call.respond(HttpStatusCode.OK, requirementsProvider.requirementsContainer)
  }

  get<API.Requirements.Requirement> {
    val requirementsProvider: RequirementsProvider by closestDI().instance()
    val requirementByID = requirementsProvider.requirementsByID[it.id]
    if (requirementByID != null) {
      call.respond(HttpStatusCode.OK, requirementByID)
    } else {
      call.respond(HttpStatusCode.NotFound, "Requirement with id '${it.id}' not found.")
    }
  }
}
