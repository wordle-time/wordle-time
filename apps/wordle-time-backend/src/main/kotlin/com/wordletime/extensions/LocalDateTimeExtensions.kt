package com.wordletime.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

fun LocalDate.Companion.now() = Clock.System.todayIn(TimeZone.currentSystemDefault())
fun LocalDate.minusDays(daysToSubtract: Long) = minus(daysToSubtract, DateTimeUnit.DAY)
