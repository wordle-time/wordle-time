package com.wordletime.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RequirementsContainer(val requirements: List<Requirement>)

@Serializable
data class Requirement(
  val id: String,
  val title: String,
  val reference: String,
  val description: String,
  val impact: String,
  val criteria: List<String>,
  val testCases: List<TestCase>,
  @Transient val resourcePath: String = "",
  val actPic: String,
  val seqPic: String
)

@Serializable
data class TestCase(
  val name: String,
  val requirement: String,
  val action: String,
  val expectation: String,
  val testPic: String
)
