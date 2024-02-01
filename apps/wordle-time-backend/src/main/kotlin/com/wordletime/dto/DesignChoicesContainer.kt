package com.wordletime.dto

import kotlinx.serialization.Serializable

@Serializable
data class DesignChoicesContainer(
  val designEntscheidungen: List<DesignDecision>
)

@Serializable
data class DesignDecision(
  val title: String,
  val description: String,
  val reasons: List<Reason>
)

@Serializable
data class Reason(
  val type: String,
  val reason: String
)
