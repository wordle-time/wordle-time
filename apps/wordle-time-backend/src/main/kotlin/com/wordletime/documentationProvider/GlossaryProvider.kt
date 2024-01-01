package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.GlossaryContainer

class GlossaryProvider {
  val glossaryContainer: GlossaryContainer = run {
    ConfigLoaderBuilder.default()
      .addResourceSource("/other/glossaries.json")
      .build()
      .loadConfigOrThrow<GlossaryContainer>()
  }
}
