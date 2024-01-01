package com.wordletime.documentationProvider

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.wordletime.dto.Requirement
import com.wordletime.dto.TestCase
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RequirementsProviderTest {
  @Test
  fun testRequirementsProvider() {
    val requirementsProvider = RequirementsProvider()

    requirementsProvider.requirementsContainer.requirements.forEach { requirement ->
      val parsedJsonObject = ConfigLoaderBuilder.default().
        addResourceSource(requirement.resourcePath + "/${requirement.id}-req.json")
        .build()
        .loadConfigOrThrow<RequirementWrapper>()

      assertEquals(parsedJsonObject, RequirementWrapper(requirement))
      assertEquals("${requirement.id}_act.png", requirement.actPic)
      assertEquals("${requirement.id}_seq.png", requirement.seqPic)
    }
  }

  @Serializable
  data class RequirementWrapper(
    val id: String,
    val title: String,
    val reference: String,
    val description: String,
    val impact: String,
    val criteria: List<String>,
    val testCases: List<TestCase>
  ) {
    constructor(requirement: Requirement) : this(
      requirement.id,
      requirement.title,
      requirement.reference,
      requirement.description,
      requirement.impact,
      requirement.criteria,
      requirement.testCases
    )
  }
}
