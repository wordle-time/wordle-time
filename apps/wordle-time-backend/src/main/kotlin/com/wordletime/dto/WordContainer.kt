package com.wordletime.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class WordContainer(
  val word: String,
  val gameID: Int,
  val date: LocalDate
) {
  override fun toString(): String = "word: '$word'; gameID: '$gameID'; date: '$date';"

  fun stripWord() = WordContainer("", gameID, date)
}
