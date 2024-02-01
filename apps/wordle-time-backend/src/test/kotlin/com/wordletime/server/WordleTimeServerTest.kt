package com.wordletime.server

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordList
import com.wordletime.config.WordProviderConfig
import com.wordletime.di.setupDI
import com.wordletime.routing.guessRouting
import com.wordletime.wordProvider.ListWordProvider
import com.wordletime.wordProvider.WordProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail
import org.kodein.di.DI
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.concurrent.CancellationException

internal class WordleTimeServerTest {

  companion object {
    /**
     * A [ServerConfig] with placeholder host, port, frontendPort and demo-mode set to true.
     */
    private val SERVER_CONFIG = ServerConfig("localhost", 8090, 7070, true)

    /**
     * A [WordProviderConfig] that provides the valid word "aaabb".
     */
    val STATIC_WORD_PROVIDER_CONFIG = WordProviderConfig("aaabb")

    /**
     * A [WordProviderConfig] that provides the invalid word "app".
     */
    val INVALID_WORD_PROVIDER_CONFIG = WordProviderConfig("app")

    /**
     * A [WordProviderConfig] that provides no word, prompting the usage of a [ListWordProvider]
     */
    val RANDOM_WORD_PROVIDER_CONFIG = WordProviderConfig("")

    /**
     * Generates a [Config] based on the [SERVER_CONFIG] and the passed [wordProviderConfig]
     */
    fun generateTestConfig(wordProviderConfig: WordProviderConfig) =
      Config(SERVER_CONFIG, wordProviderConfig)

    fun testApplicationWithSetup(config: Config, diOverrideModule: DI.Module? = null, routingExtension: Routing.() -> Unit = {}, handler: suspend (HttpClient) -> Unit) =
      testApplication {
        application {
          setupDI(config, diOverrideModule)

          installPlugins()
          routing {
            routingExtension()
            guessRouting()
          }
        }

        val client = createClient {
          install(ContentNegotiation) {
            json()
          }
          install(Resources)
        }

        handler(client)
      }
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
  @DisplayName("Test initializing DI with invalid word")
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
  @Tag("slow")
  @DisplayName("Test server startup")
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
