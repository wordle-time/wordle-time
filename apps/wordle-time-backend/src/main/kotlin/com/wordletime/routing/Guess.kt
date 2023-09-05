package com.wordletime.routing

import com.wordletime.dto.GuessLengthError
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.Phrase
import com.wordletime.dto.WordPhraseStructure
import com.wordletime.dto.WrongPhraseError
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
class Guess(override val word: String, override val phrase: String) : WordPhraseStructure

@Resource("currentPhrase")
class CurrentPhrase()


fun Application.setupAPIRouting() {
  routing {
    openAPI("openapi", swaggerFile = "wordle_time-openapi.yaml") {
      this.opts.openAPI.apply {
        this.servers = listOf(Server().apply { url = "https://localhost:8080" })
      }
    }
    swaggerUI(path = "swagger", swaggerFile = "wordle_time-openapi.yaml")

    route("/api") {
      get<Guess> {
        val wordProvider: WordProvider by closestDI().instance()
        val currentWordPhrase = wordProvider.currentWordPhrase
        when {
          it.phrase != currentWordPhrase.phrase ->
            call.respond(HttpStatusCode.BadRequest, WrongPhraseError())

          it.word.length != 5 ->
            call.respond(HttpStatusCode.BadRequest, GuessLengthError(it.word))

          else -> {
            val letterSet = currentWordPhrase.word.toSet()
            val letterStateList = it.word.mapIndexed { index, c ->
              when (c) {
                currentWordPhrase.word[index] -> LetterState.CorrectSpot
                in letterSet -> LetterState.WrongSpot
                else -> LetterState.WrongLetter
              }

            }
            call.respond(HttpStatusCode.OK, GuessResult(letterStateList))
          }
        }
      }

      get<CurrentPhrase> {
        val wordProvider: WordProvider by closestDI().instance()
        val currentWordPhrase = wordProvider.currentWordPhrase
        call.respond(HttpStatusCode.OK, Phrase(currentWordPhrase.phrase))
      }
    }
  }
}
