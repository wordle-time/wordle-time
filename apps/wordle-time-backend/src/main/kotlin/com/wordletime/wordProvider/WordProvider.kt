package com.wordletime.wordProvider

import com.wordletime.dto.WordPhrase
import kotlin.random.Random
import kotlin.random.nextInt

abstract class WordProvider {
  abstract fun getWord(): String
  private fun getRandomPhrase(): String =
    CharArray(15) { charPool[Random.nextInt(charPool.indices)] }.joinToString("")

  private fun generateWordPhraseCombo() = WordPhrase(getWord(), getRandomPhrase())

  var currentWordPhrase = generateWordPhraseCombo()
}

private val charPool = ('a'..'z').toList()

