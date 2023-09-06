package com.wordletime.wordProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.WordList
import com.wordletime.dto.WordGameID
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.random.Random
import kotlin.random.nextInt

private val charPool = ('a'..'z').toList()

private val logger = KotlinLogging.logger {}
abstract class WordProvider {
  private companion object {
    private fun randomGameID(): String =
      CharArray(15) { charPool[Random.nextInt(charPool.indices)] }.joinToString("")
  }

  abstract fun getWord(): String

  private fun generateWordGameID() = WordGameID(getWord(), randomGameID())

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

class ListWordProvider: WordProvider() {
  private val wordList = ConfigLoaderBuilder.default()
    .addResourceSource("/words.json")
    .build()
    .loadConfigOrThrow<WordList>()

  override fun getWord(): String = wordList.words.random()
}

class StaticWordProvider: WordProvider() {
  override fun getWord(): String = "apple"
}
