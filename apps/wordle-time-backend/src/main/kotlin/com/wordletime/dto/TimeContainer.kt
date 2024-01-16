package com.wordletime.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TimeContainer(
  val time: Instant
)
