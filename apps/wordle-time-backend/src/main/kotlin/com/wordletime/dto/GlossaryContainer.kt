package com.wordletime.dto

import kotlinx.serialization.Serializable

@Serializable
data class GlossaryContainer(val glossaries: List<GlossaryEntry>)

@Serializable
data class GlossaryEntry(
  val name: String,
  val description: String,
  val url: String? = null
)
