package com.wordletime.requirements

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addMapSource
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.Requirement
import com.wordletime.dto.RequirementsContainer
import java.io.File

class RequirementsProvider {
  val requirementsContainer = run {
    val requirementsDirFile = File(this.javaClass.getResource("/requirements/")!!.toURI())
    val resourceRoot = requirementsDirFile.parentFile

    requirementsDirFile
      .listFiles { dir, _ -> dir.isDirectory }!!
      .map { requirementDir ->

        val resourcePath = requirementDir.resolve("${requirementDir.name}-req.json").relativeTo(resourceRoot)
        val resourceJsonString = "/$resourcePath"
        ConfigLoaderBuilder.default()
          .addResourceSource(resourceJsonString)
          .addMapSource(mapOf("resourcePath" to "/${resourcePath.parent}"))
          .build()
          .loadConfigOrThrow<Requirement>()
      }.sortedBy { it.id }
      .let { RequirementsContainer(it) }
  }

  val requirementsByID = requirementsContainer.requirements.associateBy { it.id }
}

