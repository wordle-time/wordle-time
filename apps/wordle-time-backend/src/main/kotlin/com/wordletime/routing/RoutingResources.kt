package com.wordletime.routing

import io.ktor.resources.Resource

@Resource("/api")
class API {
  @Resource("guess")
  class Guess(val parent: API = API(), val word: String)

  @Resource("requirements")
  class Requirements(val parent: API = API()) {
    @Resource("{id}")
    class Requirement(val parent: Requirements = Requirements(), val id: String) {
      @Resource("{fileName}")
      class Pic(val parent: Requirement, val fileName: String)
    }
  }
}
