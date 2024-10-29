package dev.sobhy.babycareassistant.sleep.data.model

data class SleepData(
    val id: String = "",
    val date: String = "",
    val babyAge: String = "",
    val sleepQuality: String = "",
    val sleepTimes: List<SleepTime> = emptyList(),
    val totalSleepTime: Double = 0.0,
)
