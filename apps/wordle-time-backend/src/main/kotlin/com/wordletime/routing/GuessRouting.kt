package com.wordletime.routing

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
import io.ktor.server.routing.Routing
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.guessRouting() {
  get<API.Guess> {
    val wordState: WordState by closestDI().instance()
    val currentWordGameID = wordState.currentWordGameID

    val gameID = call.request.cookies["gameID"]
    when {
      gameID != null && gameID != currentWordGameID.gameID -> {
        val previousWordGameID = wordState.previousWordGameID
        if (gameID == previousWordGameID.gameID) {
          call.respond(HttpStatusCode.NotFound, OldGameIDError(previousWordGameID.word))
        } else {
          call.respond(HttpStatusCode.NotFound, WrongGameIDError())
        }
      }

      it.word.length != 5 ->
        call.respond(HttpStatusCode.BadRequest, GuessLengthError(it.word))

      else -> {
        val letterSet = currentWordGameID.word.toSet()
        val letterStateList = it.word.mapIndexed { index, c ->
          when (c) {
            currentWordGameID.word[index] -> LetterState.CorrectSpot
            in letterSet -> LetterState.WrongSpot
            else -> LetterState.WrongLetter
          }
        }

        call.response.cookies.append("gameID", currentWordGameID.gameID)
        call.respond(HttpStatusCode.OK, GuessResult(letterStateList))
      }
    }
  }
}
