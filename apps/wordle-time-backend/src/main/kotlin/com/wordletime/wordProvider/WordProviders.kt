package com.wordletime.wordProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.WordList

interface WordProvider {
  fun getWord(): String
}

class ListWordProvider: WordProvider {
  private val wordList = ConfigLoaderBuilder.default()
    .addResourceSource("/words.json")
    .build()
    .loadConfigOrThrow<WordList>()

  override fun getWord(): String = wordList.words.random()
}

class StaticWordProvider(private val staticWord: String): WordProvider {
  override fun getWord(): String = staticWord
}
