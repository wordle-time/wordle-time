package com.wordletime.dto

import com.wordletime.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class WordContainer(
  val word: String,
  val gameID: Int,
  @Serializable(with = LocalDateSerializer::class)
  val date: LocalDate
) {
  override fun toString(): String = "word: '$word'; gameID: '$gameID'; date: '$date';"

  fun stripWord() = WordContainer("", gameID, date)
}
