package com.wordletime.di

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.config.Config
import com.wordletime.config.ServerConfig
import com.wordletime.config.WordList
import com.wordletime.config.WordProviderConfig
import com.wordletime.documentationProvider.DesignChoicesProvider
import com.wordletime.documentationProvider.GlossaryProvider
import com.wordletime.documentationProvider.RequirementsProvider
import com.wordletime.wordProvider.ListWordProvider
import com.wordletime.wordProvider.StaticWordProvider
import com.wordletime.wordProvider.WordProvider
import com.wordletime.wordProvider.WordState
import io.ktor.server.application.Application
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

internal fun Application.setupDI(config: Config, overrideModule: DI.Module? = null) {
  di {
    import(serverModule(config))
    if (overrideModule != null) import(overrideModule, allowOverride = true)
  }
}

internal fun serverModule(config: Config) = DI.Module("serverConfig") {
  bind<Config> { instance(config) }
  bind<ServerConfig> { singleton { instance<Config>().server } }
  bind<WordProviderConfig> { singleton { instance<Config>().wordProviderConfig } }

  bind<WordList> {
    singleton {
      ConfigLoaderBuilder.default()
        .addResourceSource("/words.json")
        .build()
        .loadConfigOrThrow<WordList>()
    }
  }

  bind<WordProvider> {
    singleton {
      val wordProviderConfig: WordProviderConfig by di.instance()
      wordProviderConfig.staticWord.let { provideWord ->
        check(provideWord.isEmpty() || provideWord.length == 5) {
          "The configured wordl Word must either be empty to randomly choose a word from words.json or 5 letters long"
        }
        if (provideWord.isEmpty()) ListWordProvider(instance<WordList>()) else StaticWordProvider(provideWord)
      }
    }
  }
  bind<WordState> { singleton { WordState(instance<WordProvider>(), instance<ServerConfig>().demo) } }
  bind<RequirementsProvider> { singleton { RequirementsProvider() } }
  bind<GlossaryProvider> { singleton { GlossaryProvider() } }
  bind<DesignChoicesProvider> { singleton { DesignChoicesProvider() } }
}
