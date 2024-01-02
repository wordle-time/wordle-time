package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.DesignChoicesContainer

class DesignChoicesProvider {
  val designChoicesContainer: DesignChoicesContainer = run {
    ConfigLoaderBuilder.default()
      .addResourceSource("/other/design-choices.json")
      .build()
      .loadConfigOrThrow<DesignChoicesContainer>()
  }
}
