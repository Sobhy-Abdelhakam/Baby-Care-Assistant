package dev.sobhy.babycareassistant.sleep.add

import java.time.LocalDate
import java.time.LocalTime

sealed class AddSleepUiEvent {
    data class DateOfSleepChanged(val date: LocalDate) : AddSleepUiEvent()
    data class SleepTimeChange(val time: LocalTime) : AddSleepUiEvent()
    data class WakeUpTimeChange(val time: LocalTime) : AddSleepUiEvent()
    data class DurationChange(val duration: String) : AddSleepUiEvent()
    data object AddSleepTime : AddSleepUiEvent()
    data class OnDeleteSleepTimeClicked(val index: Int) : AddSleepUiEvent()
    data class SaveSleepData(val sleepId: String?) : AddSleepUiEvent()
}
