package com.wordletime.dto

import kotlinx.serialization.Serializable

@Serializable
data class RequirementsContainer(val requirements: List<Requirement>)

@Serializable
data class Requirement(
  val id: String,
  val title: String,
  val reference: String,
  val description: String,
  val impact: String,
  val criteria: List<String>
)
