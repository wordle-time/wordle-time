package com.wordletime.wordProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.WordList



class ListWordProvider: WordProvider() {
  private val wordList = ConfigLoaderBuilder.default()
    .addResourceSource("/words.json")
    .build()
    .loadConfigOrThrow<WordList>()

  override fun getWord(): String = wordList.words.random()
}
