package com.wordletime.wordProvider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class StaticWordProviderTest {

  companion object {
    @JvmStatic
    fun argumentsProvider(): Stream<Arguments> = Stream.of(
      Arguments.of("apple"),
      Arguments.of("steam"),
      Arguments.of("dango")
    )
  }

  @ParameterizedTest
  @MethodSource("argumentsProvider")
  fun getWord(testWord: String) {
    val staticWordProvider = StaticWordProvider(testWord)
    assertEquals(testWord, staticWordProvider.getWord())
  }
}
