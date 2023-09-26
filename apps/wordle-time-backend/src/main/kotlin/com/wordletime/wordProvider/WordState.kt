package com.wordletime.wordProvider

import com.wordletime.dto.WordContainer
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDate
import java.util.TreeMap

private val logger = KotlinLogging.logger {}
class WordState(private val wordProvider: WordProvider, private val demoMode: Boolean) {
  private var nextGameID = 1

  private val wordContainers = TreeMap<Int, WordContainer>().apply {
    if (demoMode) {
      for (pastDays in 5L downTo 1L) {
        WordContainer(wordProvider.getWord(), nextGameID++, LocalDate.now().minusDays(pastDays))
          .let { firstWordContainer ->
            this[firstWordContainer.gameID] = firstWordContainer
          }
      }
    }

    WordContainer(wordProvider.getWord(), nextGameID++, LocalDate.now()).let { firstWordContainer ->
      this[firstWordContainer.gameID] = firstWordContainer
      logger.info { "Current wordContainer: $firstWordContainer" }
    }
  }

  fun existingWordContainerByID(gameID: Int) = wordContainers[gameID]

  fun currentWordContainer(): WordContainer = wordContainers.lastEntry().value.let {
    when (it.date) {
      LocalDate.now() -> it
      else -> WordContainer(wordProvider.getWord(), nextGameID++, LocalDate.now()).also { newWordContainer ->
        wordContainers[newWordContainer.gameID] = newWordContainer
      }
    }
  }
}
