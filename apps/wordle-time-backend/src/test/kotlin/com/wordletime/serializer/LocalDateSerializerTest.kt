package com.wordletime.serializer

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.util.stream.Stream

internal class LocalDateSerializerTest {

  companion object {
    private val dateStrings = listOf("2022-08-15", "2011-01-05", "2023-02-02", "2020-01-01")

    @JvmStatic
    fun argumentsProviderSerialization(): Stream<Arguments> =
      dateStrings.map { Arguments.of(LocalDate.parse(it), Json.encodeToString(it)) }.stream()

    @JvmStatic
    fun argumentsProviderDeserialization(): Stream<Arguments> =
      dateStrings.map { Arguments.of(Json.encodeToString(it), LocalDate.parse(it)) }.stream()
  }

  @ParameterizedTest
  @MethodSource("argumentsProviderSerialization")
  fun testSerialization(localDate: LocalDate, expected: String) {
    assertEquals(expected, Json.encodeToString(LocalDateSerializer, localDate))
  }

  @ParameterizedTest
  @MethodSource("argumentsProviderDeserialization")
  fun testDeserialization(localDateString: String, expected: LocalDate) {
    assertEquals(expected, Json.decodeFromString(LocalDateSerializer, localDateString))
  }
}
