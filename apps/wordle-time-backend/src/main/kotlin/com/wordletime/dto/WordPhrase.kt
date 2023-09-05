package com.wordletime.dto

import kotlinx.serialization.Serializable

interface WordPhraseStructure {
  val word: String
  val phrase: String
}

@Serializable
data class WordPhrase(override val word: String, override val phrase: String): WordPhraseStructure
