package dev.sobhy.babycareassistant.sleep.add

import dev.sobhy.babycareassistant.sleep.data.model.SleepTime
import java.time.LocalDate
import java.time.LocalTime

data class AddSleepState(
    val date: LocalDate = LocalDate.of(1000, 1, 1),
    val sleepTime: LocalTime = LocalTime.of(10, 0),
    val wakeUpTime: LocalTime = LocalTime.of(10, 0),
    val duration: String = "",
    val sleepTimesList: List<SleepTime> = emptyList(),

    val dateError: String? = null,
    val sleepTimesError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
