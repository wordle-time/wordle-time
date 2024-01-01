package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.GlossaryContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GlossaryProviderTest {

    @Test
    fun getGlossaryContainer() {
      val glossaryProvider = GlossaryProvider()
      val expectedGlossaryContainer = ConfigLoaderBuilder.default()
        .addResourceSource("/other/glossaries.json")
        .build()
        .loadConfigOrThrow<GlossaryContainer>()

      assertEquals(expectedGlossaryContainer, glossaryProvider.glossaryContainer)
    }
}
