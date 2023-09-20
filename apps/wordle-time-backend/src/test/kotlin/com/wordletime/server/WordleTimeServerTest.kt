package com.wordletime.server

import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordProviderConfig
import com.wordletime.di.setupDI
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.WordContainer
import com.wordletime.routing.guessRouting
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class WordleTimeServerTest {
  private val testConfig = Config(
    ServerConfig("example.com", 80, 7070, true),
    WordProviderConfig("apple")
  )

  private fun testApplicationWithSetup(handler: suspend (HttpClient) -> Unit) {
    testApplication {
      application {
        setupDI(testConfig)
        installPlugins()
        routing {
          guessRouting()
        }
      }

      val client = createClient {
        install(ContentNegotiation) {
          json()
        }
      }

      handler(client)
    }
  }

  @Test
  fun testGuessRouting() = testApplicationWithSetup { client ->
    val responseAllCorrect: GuessResult = client.get("/api/guess") {
      this.parameter("word", "apple")
    }.body()

    assertIterableEquals(
      List(5) { LetterState.CorrectSpot },
      responseAllCorrect.letterStates
    )

    val responseMixed: GuessResult = client.get("/api/guess") {
      this.parameter("word", "apleb")
    }.body()

    assertIterableEquals(
      listOf(
        LetterState.CorrectSpot,
        LetterState.CorrectSpot,
        LetterState.WrongSpot,
        LetterState.WrongSpot,
        LetterState.WrongLetter
      ),
      responseMixed.letterStates
    )
  }

  @Test
  fun testWordForGameIDRouting() = testApplicationWithSetup { client ->
    //test today
    val todayResponse = client.get("/api/wordForGameID") {
      this.parameter("gameID", "6")
    }
    assertEquals(HttpStatusCode.Forbidden, todayResponse.status)

    //test days 5 prior to this day until yesterday
    for (dayMinus in 5 downTo 1) {
      val dayMinusResponse: WordContainer = client.get("/api/wordForGameID") {
        this.parameter("gameID", "${6 - dayMinus}")
      }.body()

      assertEquals(testConfig.wordProviderConfig.staticWord, dayMinusResponse.word)
      assertEquals(LocalDate.now().minusDays(dayMinus.toLong()), dayMinusResponse.date)
      assertEquals(6 - dayMinus, dayMinusResponse.gameID)
    }

    //test id before all other ids
    val beforeResponse = client.get("/api/wordForGameID") {
      this.parameter("gameID", "0")
    }
    assertEquals(HttpStatusCode.NotFound, beforeResponse.status)

    //test id after today's id
    val afterResponse = client.get("/api/wordForGameID") {
      this.parameter("gameID", "7")
    }
    assertEquals(HttpStatusCode.NotFound, afterResponse.status)
  }
}
