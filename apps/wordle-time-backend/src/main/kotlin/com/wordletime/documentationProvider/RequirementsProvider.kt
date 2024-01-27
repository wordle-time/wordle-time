package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addMapSource
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.Requirement
import com.wordletime.dto.RequirementsContainer
import com.wordletime.extensions.addIfFileExists
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
          .addMapSource(
            buildMap<String, String> {
              put("resourcePath", "/${resourcePath.parent}")

              addIfFileExists(requirementDir, "actPic", "${requirementDir.name}_act.png")
              addIfFileExists(requirementDir, "seqPic", "${requirementDir.name}_seq.png")
            }
          )
          .build()
          .loadConfigOrThrow<Requirement>()
      }.sortedBy { it.id }
      .let { RequirementsContainer(it) }
  }

  val requirementsByID = requirementsContainer.requirements.associateBy { it.id }
}



