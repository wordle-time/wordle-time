package com.wordletime.wordProvider

import kotlinx.serialization.Serializable

@Serializable
data class WordGameID(val word: String, val gameID: String) {
  override fun toString(): String = "word: '$word'; gameID: '$gameID'"
}
