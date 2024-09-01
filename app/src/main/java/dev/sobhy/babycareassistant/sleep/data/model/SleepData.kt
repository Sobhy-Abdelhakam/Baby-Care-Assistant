package dev.sobhy.babycareassistant.sleep.data.model

data class SleepData(
    val id: String = "",
    val date: String = "",
    val sleepTimes: List<SleepTime> = emptyList()
)
