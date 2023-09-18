package com.wordletime.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

interface Error {
  val message: String
}

@Serializable
class GuessLengthError(@Transient val guessedWord: String = ""): Error {
  override val message =
    "Geratenes Wort muss 5 Buchstaben lang sein. Geraten '$guessedWord' mit ${guessedWord.length} Buchstaben."
}

@Serializable
class WrongGameIDError: Error {
  override val message = "Rate-Versuch für altes Wort."
}

@Serializable
class OldGameIDError(val oldWord: String): Error {
  override val message = "Rate Versuch für altes Wort."
}

@Serializable
class GameIDNotFoundError(val gameID: Int): Error {
  override val message = "Word for gameID '$gameID' not found"
}

@Serializable
class CurrentDayWordRequestError(val gameID: Int): Error {
  override val message = "Word for latest gameID '$gameID' won't be shared"
}
