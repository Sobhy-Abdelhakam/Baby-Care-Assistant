package dev.sobhy.babycareassistant.sleep.add

import dev.sobhy.babycareassistant.sleep.data.model.SleepTime
import java.time.LocalDate
import java.time.LocalTime

data class AddSleepState(
    val date: LocalDate = LocalDate.of(1000, 1, 1),
    val babyAge: String = "",
    val sleepQuality: String = "Choose Quality",
    val sleepTime: LocalTime = LocalTime.of(0, 0, 30),
    val wakeUpTime: LocalTime = LocalTime.of(0, 0, 30),
    val duration: Long = 0L,
    val sleepTimesList: List<SleepTime> = emptyList(),
    val totalSleepTime: Double = 0.0,

    val dateError: String? = null,
    val sleepTimesError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
