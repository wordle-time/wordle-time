package com.wordletime.requirements

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.Requirements

class RequirementsProvider {
  val requirements = ConfigLoaderBuilder.default()
    .addResourceSource("/requirements.json")
    .build()
    .loadConfigOrThrow<Requirements>()
}

