package dev.sobhy.babycareassistant.breastfeeding.add

import java.time.LocalDate
import java.time.LocalTime

data class AddBreastFeedState(
    val feedingDate: LocalDate = LocalDate.of(1000, 1, 1),
    val feedingDay: String = "",
    val numOfFeedingPerDay: String = "",
    val feedingTimes: List<FeedingTimeData> = emptyList(), // to make time and amount each time

    val dateError: String? = null,
    val numberOfFeedingPerDayError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

data class FeedingTimeData(
    var feedingTime: LocalTime = LocalTime.of(0, 0, 30),
    var amountOfMilk: String = "",
)
