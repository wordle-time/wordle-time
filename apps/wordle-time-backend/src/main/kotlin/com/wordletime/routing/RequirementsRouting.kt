package com.wordletime.routing

import com.wordletime.dto.Requirement
import com.wordletime.requirements.RequirementsProvider
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.util.pipeline.PipelineContext
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.io.File

fun Route.requirementsRouting() {
  apiRequirements()
  apiRequirementsRequirement()
  apiRequirementsRequirementPic()
}

private fun Route.apiRequirements() {
  get<API.Requirements> {
    val requirementsProvider: RequirementsProvider by closestDI().instance()
    call.respond(HttpStatusCode.OK, requirementsProvider.requirementsContainer)
  }
}

private fun Route.apiRequirementsRequirement() {
  get<API.Requirements.Requirement> {
    handleRequirementByID(it.id) { requirement ->
      call.respond(HttpStatusCode.OK, requirement)
    }
  }
}

private fun Route.apiRequirementsRequirementPic() {
  get<API.Requirements.Requirement.Pic> {
    handleRequirementByID(it.parent.id) { requirement ->
      val requirementURL = this::class.java.getResource(requirement.resourcePath)!!.toURI()
      val requirementPath = File(requirementURL).toPath()

      val picFile = requirementPath.resolve(it.fileName).toFile()

      when {
        !picFile.exists() -> call.respond(HttpStatusCode.NotFound, "Picture '${it.fileName}' not found")
        !picFile.toPath().startsWith(requirementPath) -> call.respond(
          HttpStatusCode.BadRequest,
          "Selected file not child of ${it.parent.id} dir"
        )

        else -> call.respondFile(picFile)
      }
    }
  }
}

private suspend fun PipelineContext<*, ApplicationCall>.handleRequirementByID(
  id: String,
  handler: suspend (Requirement) -> Unit
) {
  val requirementsProvider: RequirementsProvider by closestDI().instance()
  val requirementByID = requirementsProvider.requirementsByID[id]
  if (requirementByID != null) {
    handler(requirementByID)
  } else {
    call.respond(HttpStatusCode.NotFound, "Requirement with id '$id' not found.")
  }
}
