package com.wordletime.documentationProvider

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
      .listFiles(File::isDirectory)!!
      .map { requirementDir ->
        //TODO: Revert actPic/seqPic from URL to filename
        val resourcePath = requirementDir.resolve("${requirementDir.name}-req.json").relativeTo(resourceRoot)
        val resourceJsonString = "/$resourcePath"
        ConfigLoaderBuilder.default()
          .addResourceSource(resourceJsonString)
          .addMapSource(mapOf(
            "resourcePath" to "/${resourcePath.parent}",
            /*
            "actPic" to "http://${serverConfig.host}:${serverConfig.port}/api/requirements/${requirementDir.name}/${requirementDir.name}_act.png",
            "seqPic" to "http://${serverConfig.host}:${serverConfig.port}/api/requirements/${requirementDir.name}/${requirementDir.name}_seq.png"
             */
            "actPic" to "${requirementDir.name}_act.png",
            "seqPic" to "${requirementDir.name}_seq.png"
            ))
          .build()
          .loadConfigOrThrow<Requirement>()
      }.sortedBy { it.id }
      .let { RequirementsContainer(it) }
  }

  val requirementsByID = requirementsContainer.requirements.associateBy { it.id }
}

