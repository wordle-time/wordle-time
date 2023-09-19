package com.wordletime.wordProvider

import com.wordletime.config.WordList
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ListWordProviderTest {

  companion object {
    @JvmStatic
    fun argumentsProvider(): Stream<Arguments> = Stream.of(
      Arguments.of(
        WordList(
          "https://example.com/list",
          listOf("apple", "steam", "dango")
        )
      ),
      Arguments.of(
        WordList(
          "https://example.com/list",
          listOf("helix", "count", "condo", "stamp")
        )
      ),
      Arguments.of(
        WordList(
          "https://example.com/list",
          listOf("graph", "sword")
        )
      ),
    )
  }

  @ParameterizedTest
  @MethodSource("argumentsProvider")
  fun getWord(wordList: WordList) {
    val listWordProvider = ListWordProvider(wordList)

    val wordListSet = wordList.words.toSet()
    repeat(10) {
      assertTrue(listWordProvider.getWord() in wordListSet)
    }
  }
}
