package com.wordletime.routing

import com.wordletime.documentationProvider.DesignChoicesProvider
import com.wordletime.documentationProvider.GlossaryProvider
import com.wordletime.dto.Requirement
import com.wordletime.dto.RequirementsContainer
import com.wordletime.documentationProvider.RequirementsProvider
import com.wordletime.dto.DesignChoicesContainer
import com.wordletime.dto.GlossaryContainer
import com.wordletime.server.WordleTimeServerTest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.io.path.pathString

internal class DocumentationRoutingTest {
  companion object {
    private fun testApplicationWithDocumentationSetup(
      diOverrideModule: DI.Module? = null,
      handler: suspend (HttpClient) -> Unit
    ) =
      WordleTimeServerTest.testApplicationWithSetup(
        WordleTimeServerTest.generateTestConfig(WordleTimeServerTest.STATIC_WORD_PROVIDER_CONFIG),
        diOverrideModule,
        { documentationRouting() },
        handler
      )

    @JvmStatic
    fun testRequirement(): Stream<Arguments> {
      val requirementsProvider = RequirementsProvider()

      val requirementsDIModule by DI.Module {
        bind(overrides = true) { instance(requirementsProvider) }
      }

      return requirementsProvider.requirementsContainer.requirements.stream().map {
        val expectedRequirementJSON = Json.encodeToString(it)
        val expectedRequirement: Requirement = Json.decodeFromString(expectedRequirementJSON)
        Arguments.of(requirementsDIModule, expectedRequirement)
      }
    }

    @JvmStatic
    fun testRequirementsRequirementPic(): Stream<Arguments> {
      val requirementsProvider = RequirementsProvider()

      val requirementsDIModule by DI.Module {
        bind(overrides = true) { instance(requirementsProvider) }
      }

      return requirementsProvider.requirementsContainer.requirements.stream().flatMap {
        buildList {
          add(it.actPic)
          add(it.seqPic)
          it.testCases.forEach {
            add(it.testPic)
          }
        }.map { picName ->
          Arguments.of(requirementsDIModule, it.id, picName, Path.of(it.resourcePath, picName).pathString)
        }.stream()
      }
    }
  }

  @Test
  fun testRequirements() {
    val requirementsProvider = RequirementsProvider()

    val expectedJson = Json.encodeToString(requirementsProvider.requirementsContainer)
    val expectedRequirementsContainer: RequirementsContainer = Json.decodeFromString(expectedJson)

    val requirementsProviderDIModule by DI.Module {
      bind(overrides = true) { instance(requirementsProvider) }
    }

    testApplicationWithDocumentationSetup(requirementsProviderDIModule) { client ->
      val receivedRequirements: RequirementsContainer = client.get(API.Documentation.Requirements()).body()

      assertEquals(expectedRequirementsContainer, receivedRequirements)
    }
  }

  @ParameterizedTest
  @MethodSource("testRequirement")
  fun testRequirementsRequirement(requirementsProviderDIModule: DI.Module, testForRequirement: Requirement) =
    testApplicationWithDocumentationSetup(requirementsProviderDIModule) { client ->
      val receivedRequirement: Requirement = client.get(API.Documentation.Requirements.Requirement(id = testForRequirement.id)).body()

      assertEquals(testForRequirement, receivedRequirement)

    }

  @ParameterizedTest
  @MethodSource("testRequirementsRequirementPic")
  fun testRequirementsRequirementPic(
    requirementsProviderDIModule: DI.Module,
    requirementID: String,
    picName: String,
    resourceLocation: String
  ) = testApplicationWithDocumentationSetup(requirementsProviderDIModule) { client ->

    val picResponse = client.get(
      API.Documentation.Requirements.Requirement.Pic(
        API.Documentation.Requirements.Requirement(id = requirementID),
        fileName = picName
      )
    )
    assertEquals(HttpStatusCode.OK, picResponse.status, "$requirementID - $picName")
    val responseByteArray = picResponse.readBytes()

    val expectedBytes = this::class.java.getResourceAsStream(resourceLocation)!!.readBytes()

    assertIterableEquals(expectedBytes.asIterable(), responseByteArray.asIterable())
  }

  @Test
  fun testGlossaries() {
    val glossaryProvider = GlossaryProvider()

    val expectedJson = Json.encodeToString(glossaryProvider.glossaryContainer)
    val expectedGlossaryContainer: GlossaryContainer = Json.decodeFromString(expectedJson)

    val glossaryProviderDIModule by DI.Module {
      bind(overrides = true) { instance(glossaryProvider) }
    }

    testApplicationWithDocumentationSetup(glossaryProviderDIModule) { client ->
      val receivedGlossary: GlossaryContainer = client.get(API.Documentation.Glossaries()).body()

      assertEquals(expectedGlossaryContainer, receivedGlossary)
    }
  }

  @Test
  fun testDesignChoices() {
    val designChoicesProvider = DesignChoicesProvider()

    val expectedJson = Json.encodeToString(designChoicesProvider.designChoicesContainer)
    val expectedDesignChoicesContainer: DesignChoicesContainer = Json.decodeFromString(expectedJson)

    val designChoicesProviderDIModule by DI.Module {
      bind(overrides = true) { instance(designChoicesProvider) }
    }

    testApplicationWithDocumentationSetup(designChoicesProviderDIModule) { client ->
      val receivedDesignChoices: DesignChoicesContainer = client.get(API.Documentation.DesignChoices()).body()

      assertEquals(expectedDesignChoicesContainer, receivedDesignChoices)
    }
  }
}
