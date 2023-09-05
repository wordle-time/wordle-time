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

class WrongPhraseError: Error {
  override val message = "Rate-Versuch für veraltetes Wort."

}
