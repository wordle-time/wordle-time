package com.wordletime.wordProvider

import com.wordletime.words.WordGameID
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.random.Random
import kotlin.random.nextInt

private val logger = KotlinLogging.logger {}
class WordState(private val wordProvider: WordProvider) {
  private companion object {
    private val charPool = ('a'..'z').toList()

    private fun randomGameID(): String =
      CharArray(15) { charPool[Random.nextInt(charPool.indices)] }.joinToString("")
  }

  private fun generateWordGameID() = WordGameID(wordProvider.getWord(), randomGameID())

  var previousWordGameID: WordGameID = generateWordGameID()
    set(value) {
      logger.info { "previousWordGameID: $value" }
      field = value
    }
  var currentWordGameID: WordGameID = generateWordGameID()
    set(value) {
      logger.info { "currentWordGameID: $value" }
      field = value
    }

  init {
    logger.info { "previousWordGameID: $previousWordGameID" }
    logger.info { "currentWordGameID: $currentWordGameID" }
  }

  fun regenerateWordGameID() {
    previousWordGameID = currentWordGameID
    currentWordGameID = generateWordGameID()
  }
}
