package com.wordletime.server

import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordProviderConfig
import com.wordletime.di.setupDI
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.routing.guessRouting
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test

class WordleTimeServerTest {
  private val testConfig = Config(
    ServerConfig("example.com", 80, 7070, true),
    WordProviderConfig("apple")
  )

  @Test
  fun testGuessRouting() = testApplication {
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


    val response: GuessResult = client.get("/api/guess") {
      this.parameter("word", "apple")
    }.body()

    assertIterableEquals(
      List(5) { LetterState.CorrectSpot },
      response.letterStates
    )
  }
}
