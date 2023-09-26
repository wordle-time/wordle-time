package com.wordletime.wordProvider

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class WordStateTest {
  private val demoWord = "apple"

  @Test
  fun testWordStateNonDemo() {
    val wordProvider = mockk<WordProvider>()
    every { wordProvider.getWord() } returns demoWord

    val wordState = WordState(wordProvider, false)
    val currentWordContainer = wordState.currentWordContainer()
    assertEquals(demoWord, currentWordContainer.word)
    assertEquals(LocalDate.now(), currentWordContainer.date)
    assertEquals(1, currentWordContainer.gameID)

    assertNull(wordState.existingWordContainerByID(2))
    assertNull(wordState.existingWordContainerByID(3))
    assertNull(wordState.existingWordContainerByID(0))

    assertEquals(currentWordContainer, wordState.existingWordContainerByID(currentWordContainer.gameID))
  }

  @Test
  fun testWordStateDemo() {
    val wordProvider = mockk<WordProvider>()
    every { wordProvider.getWord() } returns demoWord

    val wordState = WordState(wordProvider, true)
    val currentWordContainer = wordState.currentWordContainer()
    assertEquals(demoWord, currentWordContainer.word)
    assertEquals(LocalDate.now(), currentWordContainer.date)
    assertEquals(6, currentWordContainer.gameID)
    assertEquals(currentWordContainer, wordState.existingWordContainerByID(currentWordContainer.gameID))

    for(minusDays in 5 downTo 1) {
      val preContainer = wordState.existingWordContainerByID(currentWordContainer.gameID - minusDays)
      assertNotNull(preContainer)
      checkNotNull(preContainer) //smart cast preContainer to not-null henceforth

      assertEquals(demoWord, preContainer.word)
      assertEquals(LocalDate.now().minusDays(minusDays.toLong()), preContainer.date)
      assertEquals(6 - minusDays, preContainer.gameID)
    }

    assertNull(wordState.existingWordContainerByID(7))
    assertNull(wordState.existingWordContainerByID(0))
  }
}
