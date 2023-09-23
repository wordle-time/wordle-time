package com.wordletime.server

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordList
import com.wordletime.config.WordProviderConfig
import com.wordletime.di.setupDI
import com.wordletime.dto.GuessResult
import com.wordletime.dto.LetterState
import com.wordletime.dto.WordContainer
import com.wordletime.routing.guessRouting
import com.wordletime.wordProvider.ListWordProvider
import com.wordletime.wordProvider.WordProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.time.LocalDate
import java.util.concurrent.CancellationException

class WordleTimeServerTest {

  private companion object {
    /**
     * A [ServerConfig] with placeholder host, port, frontendPort and demo-mode set to true.
     */
    private val SERVER_CONFIG = ServerConfig("localhost", 8090, 7070, true)

    /**
     * A [WordProviderConfig] that provides the valid word "aaabb".
     */
    private val STATIC_WORD_PROVIDER_CONFIG = WordProviderConfig("aaabb")

    /**
     * A [WordProviderConfig] that provides the invalid word "app".
     */
    private val INVALID_WORD_PROVIDER_CONFIG = WordProviderConfig("app")

    /**
     * A [WordProviderConfig] that provides no word, prompting the usage of a [ListWordProvider]
     */
    private val RANDOM_WORD_PROVIDER_CONFIG = WordProviderConfig("")

    /**
     * Generates a [Config] based on the [SERVER_CONFIG] and the passed [wordProviderConfig]
     */
    private fun generateTestConfig(wordProviderConfig: WordProviderConfig) =
      Config(SERVER_CONFIG, wordProviderConfig)
  }


  private fun testApplicationWithSetup(config: Config, handler: suspend (HttpClient) -> Unit) {
    testApplication {
      application {
        setupDI(config)
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
  fun testGuessRouting() = testApplicationWithSetup(generateTestConfig(STATIC_WORD_PROVIDER_CONFIG)) { client ->
    val responseAllCorrect: GuessResult = client.get("/api/guess") {
      this.parameter("word", STATIC_WORD_PROVIDER_CONFIG.staticWord)
    }.body()

    assertIterableEquals(
      List(5) { LetterState.CorrectSpot },
      responseAllCorrect.letterStates
    )

    val responseMixed: GuessResult = client.get("/api/guess") {
      this.parameter("word", "ababc")
    }.body()

    assertIterableEquals(
      listOf(
        LetterState.CorrectSpot,
        LetterState.WrongSpot,
        LetterState.CorrectSpot,
        LetterState.CorrectSpot,
        LetterState.WrongLetter
      ),
      responseMixed.letterStates
    )
  }

  @Test
  fun testRandomWordProvider() = testApplication {
    val randomWordProviderDIConfig = generateTestConfig(RANDOM_WORD_PROVIDER_CONFIG)
    application {
      setupDI(randomWordProviderDIConfig)

      val expectedWords = ConfigLoaderBuilder.default()
        .addResourceSource("/words.json")
        .build()
        .loadConfigOrThrow<WordList>()
        .words.toSet()

      val wordProvider: WordProvider by closestDI().instance()

      repeat(20) {
        val chosenWord = wordProvider.getWord()
        if (chosenWord !in expectedWords) {
          fail { "Generated word '$chosenWord' not part of words.json" }
        }
      }
    }
  }

  @Test
  fun testDIInvalidWord() = testApplication {
    val invalidWordDIConfig = generateTestConfig(INVALID_WORD_PROVIDER_CONFIG)
    application {
      setupDI(invalidWordDIConfig)
      val exception = assertThrows<IllegalStateException> {
        val wordProvider by closestDI().instance<WordProvider>()
        wordProvider.getWord()
      }
      assertEquals(
        "The configured wordl Word must either be empty to randomly choose a word from words.json or 5 letters long",
        exception.message
      )
    }
  }

  @Test
  fun testWordForGameIDRouting() = testApplicationWithSetup(generateTestConfig(STATIC_WORD_PROVIDER_CONFIG)) { client ->
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

      assertEquals(STATIC_WORD_PROVIDER_CONFIG.staticWord, dayMinusResponse.word)
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

  @Test
  @Tag("slow")
  fun testServerStartup(): Unit = runBlocking {
    val testScope = CoroutineScope(Dispatchers.Default)

    val wordleTimeServer = WordleTimeServer(generateTestConfig(STATIC_WORD_PROVIDER_CONFIG), testScope)
    wordleTimeServer.startServer()


    testScope.launch {
      val serverReady = wordleTimeServer.serverState
        .firstOrNull { it == WordleTimeServer.ServerState.ServerReady }

      assertEquals(WordleTimeServer.ServerState.ServerReady, serverReady)
    }.join()

    testScope.cancel(CancellationException("Test cancellation"))
  }
}
