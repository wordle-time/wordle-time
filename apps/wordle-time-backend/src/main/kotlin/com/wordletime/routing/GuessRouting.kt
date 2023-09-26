package com.wordletime.routing

import com.wordletime.dto.CurrentDayWordRequestError
import com.wordletime.dto.GameIDNotFoundError
import com.wordletime.dto.GuessLengthError
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.OldGameIDError
import com.wordletime.dto.WrongGameIDError
import com.wordletime.wordProvider.WordState
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.time.LocalDate

fun Route.guessRouting() {
  apiGuessWord()
  apiGuessWordForGameID()
  apiGuessCurrentGameID()
}

private fun Route.apiGuessWord() {
  get<API.Guess.Word> {
    val wordState: WordState by closestDI().instance()
    val currentWordContainer = wordState.currentWordContainer()

    val gameID = call.request.cookies["gameID"]?.toInt()
    when {
      gameID != null && gameID != currentWordContainer.gameID -> {
        val previousWordContainer = wordState.existingWordContainerByID(gameID)
        if (previousWordContainer != null) {
          call.respond(HttpStatusCode.NotFound, OldGameIDError(gameID, previousWordContainer.word))
        } else {
          call.respond(HttpStatusCode.NotFound, WrongGameIDError(gameID))
        }
      }

      it.word.length != 5 ->
        call.respond(HttpStatusCode.BadRequest, GuessLengthError(it.word))

      else -> {
        val letterSet = currentWordContainer.word.toSet()
        val letterStateList = it.word.mapIndexed { index, c ->
          when (c) {
            currentWordContainer.word[index] -> LetterState.CorrectSpot
            in letterSet -> LetterState.WrongSpot
            else -> LetterState.WrongLetter
          }
        }

        call.response.cookies.append("gameID", currentWordContainer.gameID.toString())
        call.respond(HttpStatusCode.OK, GuessResult(letterStateList))
      }
    }
  }
}

private fun Route.apiGuessWordForGameID() {
  get<API.Guess.WordForGameID> {
    val wordState: WordState by closestDI().instance()
    val wordContainerForGameID = wordState.existingWordContainerByID(it.gameID)
    when {
      wordContainerForGameID == null -> call.respond(HttpStatusCode.NotFound, GameIDNotFoundError(it.gameID))

      wordContainerForGameID.date == LocalDate.now() -> call.respond(
        HttpStatusCode.Forbidden,
        CurrentDayWordRequestError(it.gameID)
      )

      else -> call.respond(HttpStatusCode.OK, wordContainerForGameID)
    }
  }
}

private fun Route.apiGuessCurrentGameID() {
  get<API.Guess.CurrentGameID> {
    val wordState: WordState by closestDI().instance()
    call.respond(wordState.currentWordContainer().stripWord())
  }
}
