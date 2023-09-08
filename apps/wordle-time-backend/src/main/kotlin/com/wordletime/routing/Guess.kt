package com.wordletime.routing

import com.wordletime.dto.GuessLengthError
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.OldGameIDError
import com.wordletime.dto.WrongGameIDError
import com.wordletime.wordProvider.WordProvider
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.swagger.v3.oas.models.servers.Server
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

@Resource("guess")
class Guess(val word: String)

//todo Kay
fun Application.setupAPIRouting() {
  routing {
    openAPI("openapi", swaggerFile = "wordle_time-openapi.yaml") {
      this.opts.openAPI.apply {
        this.servers = listOf(Server().apply { url = "https://localhost:8090" })
      }
    }
    swaggerUI(path = "swagger", swaggerFile = "wordle_time-openapi.yaml")

    route("/api") {
      get<Guess> {
        val wordProvider: WordProvider by closestDI().instance()
        val currentWordGameID = wordProvider.currentWordGameID

        val gameID = call.request.cookies["gameID"]
        when {
          gameID != null && gameID != currentWordGameID.gameID -> {
            val previousWordGameID = wordProvider.previousWordGameID
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
  }
}
