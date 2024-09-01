package dev.sobhy.babycareassistant.diapers.add

import java.time.LocalDate
import java.time.LocalTime

data class AddDiaperState(
    val diaperDate: LocalDate = LocalDate.of(1000, 1, 1),
    val diaperDay: String = "",
    val numOfDiaperChange: String = "",
    val timesOfDiaperChange: List<LocalTime> = emptyList(),

    val dateError: String? = null,
    val numOfDiaperChangeError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
