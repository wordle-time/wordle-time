package com.wordletime.wordProvider

import com.wordletime.config.WordList

interface WordProvider {
  fun getWord(): String
}


class ListWordProvider(private val wordList: WordList): WordProvider {
  override fun getWord(): String = wordList.words.random()
}

class StaticWordProvider(private val staticWord: String): WordProvider {
  override fun getWord(): String = staticWord
}
