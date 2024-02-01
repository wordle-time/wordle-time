package com.wordletime.routing

import com.wordletime.di.setupDI
import com.wordletime.dto.GuessLengthError
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.OldGameIDError
import com.wordletime.dto.TimeContainer
import com.wordletime.dto.WordContainer
import com.wordletime.extensions.minusDays
import com.wordletime.extensions.now
import com.wordletime.server.WordleTimeServerTest
import com.wordletime.server.WordleTimeServerTest.Companion.STATIC_WORD_PROVIDER_CONFIG
import com.wordletime.server.WordleTimeServerTest.Companion.generateTestConfig
import com.wordletime.server.WordleTimeServerTest.Companion.testApplicationWithSetup
import com.wordletime.server.installPlugins
import com.wordletime.wordProvider.WordState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.cookie
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.InternalPlatformDsl.toStr
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.time.ZoneId
import java.util.stream.Stream

internal class GuessRoutingTest {
  companion object {
    @JvmStatic
    fun testGuessWordRoutingOK(): Stream<Arguments> = Stream.of(
      Arguments.of(STATIC_WORD_PROVIDER_CONFIG.staticWord, List(5) { LetterState.CorrectSpot }),
      Arguments.of(
        "ababc", listOf(
          LetterState.CorrectSpot,
          LetterState.WrongSpot,
          LetterState.CorrectSpot,
          LetterState.CorrectSpot,
          LetterState.WrongLetter
        )
      )
    )

    @JvmStatic
    fun testGuessWordRoutingGuessLengthError(): Stream<Arguments> = Stream.of(
      Arguments.of("app"),
      Arguments.of("LongWord")
    )
  }

  private fun testApplicationWithGuessSetup(handler: suspend (HttpClient) -> Unit) =
    testApplicationWithSetup(
      generateTestConfig(STATIC_WORD_PROVIDER_CONFIG),
      null,
      { guessRouting() },
      handler
    )

  @Nested
  inner class TestGuessWordRouting {
    @ParameterizedTest
    @MethodSource("com.wordletime.routing.GuessRoutingTest#testGuessWordRoutingOK")
    fun testGuessWordRoutingOK(word: String, expected: List<LetterState>) =
      testApplicationWithGuessSetup { client ->
        val responseAllCorrect: GuessResult = client.get(API.Guess.Word(word = word)).body()

        assertIterableEquals(expected, responseAllCorrect.letterStates)
      }

    @ParameterizedTest
    @MethodSource("com.wordletime.routing.GuessRoutingTest#testGuessWordRoutingGuessLengthError")
    fun testGuessWordRoutingGuessLengthError(word: String) =
      testApplicationWithGuessSetup { client ->
        val expectedErrorOriginal = GuessLengthError(word)
        val expectedErrorString = Json.encodeToString(expectedErrorOriginal)
        val expectedError: GuessLengthError = Json.decodeFromString(expectedErrorString)

        val responseGuessLengthError = client.get(API.Guess.Word(word = word))

        assertEquals(HttpStatusCode.BadRequest, responseGuessLengthError.status)

        val guessLengthError: GuessLengthError = responseGuessLengthError.body()

        assertEquals(expectedError, guessLengthError)
      }

    @Test
    fun testGuessWordRoutingOldGameError() {
      testApplicationWithGuessSetup { client ->
        val oldWord = "spark"
        val oldGameID = 1

        val expectedErrorOriginal = OldGameIDError(1, STATIC_WORD_PROVIDER_CONFIG.staticWord)
        val expectedErrorString = Json.encodeToString(expectedErrorOriginal)
        val expectedError: OldGameIDError = Json.decodeFromString(expectedErrorString)

        val responseOldGameError = client.get(API.Guess.Word(word = oldWord)) {
          cookie("gameID", oldGameID.toStr())
        }

        assertEquals(HttpStatusCode.NotFound, responseOldGameError.status)

        val guessLengthError: OldGameIDError = responseOldGameError.body()

        assertEquals(expectedError, guessLengthError)
      }
    }

  }



  @Nested
  inner class TestWordForGameIDRouting {
    @Test
    @DisplayName("Test getting forbidden wordl solution for today")
    fun testForbidden() =
      testApplicationWithGuessSetup { client ->
        val currentWordState: WordContainer = client.get(API.Guess.CurrentGameID()).body()

        //test today
        val todayResponse = client.get(API.Guess.WordForGameID(gameID = currentWordState.gameID))
        assertEquals(HttpStatusCode.Forbidden, todayResponse.status)
      }

    @Test
    @DisplayName("Test getting wordl solution for invalid gameIDs")
    fun testInvalid() =
      testApplicationWithGuessSetup { client ->
        //test id before all other ids
        val beforeResponse = client.get(API.Guess.WordForGameID(gameID = 0))
        assertEquals(HttpStatusCode.NotFound, beforeResponse.status)

        val currentWordState: WordContainer = client.get(API.Guess.CurrentGameID()).body()

        //test id after today's id
        val afterResponse = client.get(API.Guess.WordForGameID(gameID = currentWordState.gameID + 1))
        assertEquals(HttpStatusCode.NotFound, afterResponse.status)
      }

    @Test
    @DisplayName("Test getting wordl solution for valid gameIDs")
    fun testAPIWordForGameIDRoutingOK() =
      testApplicationWithGuessSetup { client ->
        val currentWordState: WordContainer = client.get(API.Guess.CurrentGameID()).body()

        //test days 5 prior to this day until yesterday
        for (gameID in 1 until currentWordState.gameID) {
          val dayMinusResponse: WordContainer = client.get(API.Guess.WordForGameID(gameID = gameID)).body()

          assertEquals(STATIC_WORD_PROVIDER_CONFIG.staticWord, dayMinusResponse.word)
          assertEquals(LocalDate.now().minusDays((currentWordState.gameID - gameID).toLong()), dayMinusResponse.date)
          assertEquals(gameID, dayMinusResponse.gameID)
        }
      }

  }

  @Test
  @DisplayName("Test getting the current gameID")
  fun testAPIGuessCurrentGameID() = testApplication {
    val staticWordConfig = generateTestConfig(WordleTimeServerTest.RANDOM_WORD_PROVIDER_CONFIG)
    var currentWordContainer: WordContainer? = null
    application {
      setupDI(staticWordConfig)
      installPlugins()
      routing {
        apiRouting()
      }

      val wordState: WordState by closestDI().instance()
      currentWordContainer = wordState.currentWordContainer()
    }

    val client = createClient {
      install(ContentNegotiation) {
        json()
      }
      install(Resources)
    }

    val apiGameIDWordContainer: WordContainer = client.get(API.Guess.CurrentGameID()).body()
    assertEquals(currentWordContainer?.stripWord(), apiGameIDWordContainer)
  }

  @Test
  @DisplayName("Test getting timestamp for next word")
  fun testAPINextWordAt() = testApplication {
    val staticWordConfig = generateTestConfig(WordleTimeServerTest.RANDOM_WORD_PROVIDER_CONFIG)
    var currentWordContainer: WordContainer? = null
    application {
      setupDI(staticWordConfig)
      installPlugins()
      routing {
        apiRouting()
      }

      val wordState: WordState by closestDI().instance()
      currentWordContainer = wordState.currentWordContainer()
    }

    val client = createClient {
      install(ContentNegotiation) {
        json()
      }
      install(Resources)
    }

    val apiNextWordAtTimeContainer: TimeContainer = client.get(API.Guess.NextWordAt()).body()

    val currentZoneId = ZoneId.systemDefault()
    val zoneOffset = currentZoneId.rules.getOffset(java.time.Instant.now())

    val expectedInstant = currentWordContainer!!.date.toJavaLocalDate()
      .plusDays(1).atStartOfDay()
      .toInstant(zoneOffset)
      .toKotlinInstant()

    assertEquals(expectedInstant, apiNextWordAtTimeContainer.time)
  }

}
