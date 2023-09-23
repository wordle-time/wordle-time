package com.wordletime.routing

import io.ktor.resources.Resource

@Resource("/api")
class API {
  @Resource("guess")
  class Guess(val parent: API = API()) {
    @Resource("word")
    class Word(val parent: Guess = Guess(), val word: String)

    @Resource("wordForGameID")
    class WordForGameID(val parent: Guess = Guess(), val gameID: Int)

    @Resource("currentGameID")
    class CurrentGameID(val parent: Guess = Guess())
  }

  @Resource("requirements")
  class Requirements(val parent: API = API()) {
    @Resource("{id}")
    class Requirement(val parent: Requirements = Requirements(), val id: String) {
      @Resource("{fileName}")
      class Pic(val parent: Requirement, val fileName: String)
    }
  }
}
