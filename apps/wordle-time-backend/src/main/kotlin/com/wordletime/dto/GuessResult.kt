package com.wordletime.dto

import kotlinx.serialization.Serializable

enum class LetterState {
  CorrectSpot, WrongSpot, WrongLetter
}

@Serializable
data class GuessResult(val letterStates: List<LetterState>)
