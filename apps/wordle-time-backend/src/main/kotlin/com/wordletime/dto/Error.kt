package com.wordletime.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

interface Error {
  val message: String
}

@Serializable
data class GuessLengthError(@Transient val guessedWord: String = ""): Error {
  override val message =
    "Geratenes Wort muss 5 Buchstaben lang sein. Geraten '$guessedWord' mit ${guessedWord.length} Buchstaben."
}

@Serializable
data class WrongGameIDError(@Transient val oldGameID: Int = 0): Error {
  override val message = "Rate-Versuch für altes Wort. (gameID=$oldGameID)"
}

@Serializable
data class OldGameIDError(@Transient val oldGameID: Int = 0, val oldWord: String): Error {
  override val message = "Rate Versuch für altes Wort. (gameID=$oldGameID)"
}

@Serializable
data class GameIDNotFoundError(@Transient val gameID: Int = 0): Error {
  override val message = "Word for gameID '$gameID' not found"
}

@Serializable
data class CurrentDayWordRequestError(@Transient val gameID: Int = 0): Error {
  override val message = "Word for latest gameID '$gameID' won't be shared"
}
