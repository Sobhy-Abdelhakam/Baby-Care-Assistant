package dev.sobhy.babycareassistant.breastfeeding.add

import java.time.LocalDate
import java.time.LocalTime

data class AddBreastFeedState(
    val feedingDate: LocalDate = LocalDate.of(1000, 1, 1),
    val feedingDay: String = "",
    val numOfFeedingPerDay: String = "",
    val amountOfFeedingPerTime: String = "",
    val timesValues: List<LocalTime> = emptyList(),

    val dateError: String? = null,
    val numberOfFeedingPerDayError: String? = null,
    val amountPerTimeError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
