package com.wordletime.requirements

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.RequirementsContainer

class RequirementsProvider {
  val requirementsContainer = ConfigLoaderBuilder.default()
    .addResourceSource("/requirements.json")
    .build()
    .loadConfigOrThrow<RequirementsContainer>()

  val requirementsByID = requirementsContainer.requirements.associateBy { it.id }
}

