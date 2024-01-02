package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.DesignChoicesContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DesignChoicesProviderTest {

  @Test
  fun getDesignChoicesContainer() {
    val designChoicesProvider = DesignChoicesProvider()
    val expectedDesignChoicesContainer = ConfigLoaderBuilder.default()
      .addResourceSource("/other/design-choices.json")
      .build()
      .loadConfigOrThrow<DesignChoicesContainer>()

    assertEquals(expectedDesignChoicesContainer, designChoicesProvider.designChoicesContainer)
  }
}
